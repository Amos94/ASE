package com.advanced.software.engineering.aseproject;


import Index.IInvertedIndex;
import Index.IndexDocument;
import Index.IndexDocumentExtractionVisitor;
import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.rsse.calls.AbstractCallsRecommender;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toMap;

public class Recommender extends AbstractCallsRecommender<IndexDocument> {

    private final IInvertedIndex index;
    private List<IndexDocument> documents;
    private Logger logger = Logger.getLogger(Recommender.class.getName());

    private Map<IndexDocument, Double> candidates;
    private List<Integer> correctRecommendations = new LinkedList<>();
    private List<Integer> methodCalls = new LinkedList<>();

    private String projectName;


    Recommender(IInvertedIndex index) {
        logger.log(Level.INFO, "Initializing the recommender.");
        this.index = index;
        logger.log(Level.INFO, "Fetching identifiers from db...");
        getIndexes(); //first let's retrieve all indexes from db
    }

    Recommender(IInvertedIndex index, String projectName) {
        this.projectName = projectName;

        this.index = index;
        getIndexes(projectName); //first let's retrieve all indexes from db
    }

//    public void setProjectName(String projectName){
//        this.projectName = projectName;
//    }


    /**
     * Method to process a certain query to the recommender
     *
     * @param query - query
     * @return - return pair of member and score
     */
    @Override
    public Set<Pair<IMemberName, Double>> query(IndexDocument query) {
        Set<Pair<IMemberName, Double>> result = new LinkedHashSet<>();

        if(
                (query.getMethodCall() != "" || query.getMethodCall() != null || query.getMethodCall() != "unknown") &&
                (query.getOverallContext().size() > 0)
        ) {
            getScoredDocuments(query);
            //System.out.println(query.getMethodCall());
            methodCalls.add(1);
            for (Map.Entry<IndexDocument, Double> e : candidates.entrySet()) {
                result.add(Pair.of(e.getKey().getMethod(), e.getValue()));
                logger.log(Level.INFO, "\nFor " + query.getMethodCall() + " our recommendation is: " + e.getKey().getMethod().getName() + " confident: " + e.getValue());

                if(e.getKey().getMethod().getName().equals(query.getMethodCall()))
                    correctRecommendations.add(1);
                //System.out.println(correctRecommendations.size());
            }
        }

        return result;
    }

    /**
     *
     *
     * @param ctx - context
     * @return - return member and score
     */
    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx) {
        ISST sst = ctx.getSST();
        ISSTNodeVisitor visitor = new IndexDocumentExtractionVisitor();
        List<IndexDocument> methodInvocations = new LinkedList<>();
        sst.accept(visitor, methodInvocations);
        IndexDocument queryDocument = combineContexts(methodInvocations);
        return query(queryDocument);
    }

    /**
     *
     * @param contexts - contexts
     * @return - return document
     */
    private IndexDocument combineContexts(List<IndexDocument> contexts) {
        String lastType;
        String lastMethod;
        List<String> overallContext = new LinkedList<>();

        if (contexts.size() > 0 && !contexts.get((contexts.size() - 1)).getType().equals("") && (contexts.get((contexts.size() - 1)).getOverallContext().size() > 0)) {
            lastType = contexts.get(contexts.size() - 1).getType();
            lastMethod = contexts.get(contexts.size() - 1).getMethod().getName();
            overallContext.addAll(contexts.get(contexts.size() - 1).getOverallContext());
        }
        else {
            lastType = "unknown";
            lastMethod = "unknown";
        }

//        for (IndexDocument doc : contexts) {
//            combinedOverallContext.addAll(doc.getOverallContext());
//        }
        return new IndexDocument(lastMethod, lastType, overallContext);
    }

    /**
     *
     * @return - return model size
     */
    @Override
    public int getLastModelSize() {
        try {
            return FileUtils.sizeOfAsBigInteger(new File(Configuration.INDEX_STORAGE)).intValueExact();
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     *
     * @param ctx - context
     * @param ideProposals - proposals
     * @return - return member name and score
     */
    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
        return query(ctx);
    }

    /**
     * Method
     */
    private void getIndexes() {
        documents = index.deserializeAll();
    }

    private void getIndexes(String projectName) {
        documents = index.deserializeByProject(projectName);
    }

    /**
     *
     * @param queryDoc - query document
     */
    private void getScoredDocuments(IndexDocument queryDoc) {
        Map<IndexDocument, Double> scoredDocuments = new HashMap<>();

        //score documents using jaccard similarity score
        for (IndexDocument doc : documents) {
            Evaluator evaluator = new Evaluator(doc, queryDoc);

            double similarityScore = evaluator.calculateJaccard();
            scoredDocuments.put(doc, similarityScore);
        }

        candidates = scoredDocuments.entrySet()
                .stream()
                .distinct()
                .filter(distinctByKey(p->p.getKey().getMethod().getName()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(Configuration.MAX_CANDIDATES)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, HashMap::new));

        //candidates.putAll(findGreatest(candidates,Configuration.MAX_CANDIDATES));

    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    int getNumberOfCorrectRecommendations(){
        return correctRecommendations.size();
    }
    int getNumberMethodCalls(){
        return methodCalls.size();
    }


//    private static <K, V extends Comparable<? super V>> HashMap<K, V> findGreatest(Map<K, V> map, int n)
//    {
//        Comparator<? super Entry<K, V>> comparator =
//                (Comparator<Entry<K, V>>) (e0, e1) -> {
//                    V v0 = e0.getValue();
//                    V v1 = e1.getValue();
//                    return v0.compareTo(v1);
//                };
//
//        PriorityQueue<Entry<K, V>> highest = new PriorityQueue<>(n, comparator);
//        //highest.stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
//
//        for (Entry<K, V> entry : map.entrySet())
//        {
//            highest.offer(entry);
//            while (highest.size() > n)
//            {
//                highest.poll();
//            }
//        }
//
//        HashMap<K, V> result = new HashMap<>();
//        while (highest.size() > 0)
//        {
//            result.put(highest.poll().getKey(), Objects.requireNonNull(highest.poll()).getValue());
//        }
//        return result;
//    }
}

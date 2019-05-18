package com.advanced.software.engineering.aseproject;


import Index.IInvertedIndex;
import Index.IndexDocument;
import Index.IndexDocumentExtractionVisitor;
import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.rsse.calls.AbstractCallsRecommender;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import java.io.File;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Recommender extends AbstractCallsRecommender<IndexDocument> {

    private final IInvertedIndex index;
    private List<IndexDocument> documents;
    private Map<IndexDocument, Double>  candidates;


    public Recommender(IInvertedIndex index) {
        this.index = index;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(IndexDocument query) {

        processQuery(query);
        Set<Pair<IMemberName, Double>> result = new LinkedHashSet<>();
        getScoredDocuments(query);
        //get top 10 candidates
        for(Map.Entry<IndexDocument, Double> e:candidates.entrySet()) {
            result.add(Pair.of(e.getKey().getMethod(), e.getValue()));
            System.out.println(e.getKey().getMethod().getName());
        }

        return result;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx) {
        ISST sst = ctx.getSST();
        ISSTNodeVisitor visitor = new IndexDocumentExtractionVisitor();
        List<IndexDocument> methodInvocations = new LinkedList<>();
        sst.accept(visitor, methodInvocations);
        IndexDocument queryDocument = combineContexts(methodInvocations);

        return query(queryDocument);
    }

    private IndexDocument combineContexts(List<IndexDocument> contexts) {
        String lastType = contexts.get(contexts.size() - 1).getType();
        List<String> combinedOverallContext = new LinkedList<>();
        for (IndexDocument doc : contexts) {
            combinedOverallContext.addAll(doc.getOverallContext());
        }
        return new IndexDocument(null, lastType, combinedOverallContext);
    }

    @Override
    public int getLastModelSize() {
        try {
            return FileUtils.sizeOfAsBigInteger(new File(Configuration.INDEX_STORAGE)).intValueExact();
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
        return query(ctx);
    }

    private void getIndexes() {
        documents = index.deserializeAll();
    }

    private void getScoredDocuments(IndexDocument queryDoc){
        getIndexes(); //first let's retrieve all indexes from db

        Map<IndexDocument, Double> scoredDocuments = new HashMap<>();

        //score documents using jaccard similarity score
        for(IndexDocument doc:documents){
            Evaluator evaluator = new Evaluator(doc, queryDoc);
            double similarityScore = evaluator.calculateJaccard();
            scoredDocuments.put(doc,similarityScore);
        }

        candidates = scoredDocuments.entrySet()
                    .stream()
                    .limit(Configuration.MAX_CANDIDATES)
                    .distinct()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, HashMap::new));

    }

    private void processQuery(IndexDocument query){

    }


}

package com.advanced.software.engineering.aseproject;


import Index.IInvertedIndex;
import Index.IndexDocument;
import Index.IndexDocumentExtractionVisitor;
import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.rsse.calls.AbstractCallsRecommender;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.groups.Tuple;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Recommender extends AbstractCallsRecommender<IndexDocument> {

    private final IInvertedIndex index;


    public Recommender(IInvertedIndex index) {
        this.index = index;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(IndexDocument query) {
        processQuery(query);
        Set<Pair<IMethodName, Double>> result = new LinkedHashSet<>();
        final int CANDIDATES_TO_SUGGEST = 5;
        //get 5 candidates
        return null;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx) {
        ISST sst = ctx.getSST();
        ISSTNodeVisitor visitor = new IndexDocumentExtractionVisitor();
        List<IndexDocument> methodInvocations = new LinkedList<>();
        sst.accept(visitor, methodInvocations);
        IndexDocument queryDocument = null;
        return query(queryDocument);
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

    private void processQuery(IndexDocument receiverObj) {

    }

}

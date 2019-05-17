package com.advanced.software.engineering.aseproject;

import Index.IInvertedIndex;
import Index.IndexDocumentExtractionVisitorNoList;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class SSTProcessor {

    private IInvertedIndex index = null;

    /**
     * Create InvertedIndex
     */
    public SSTProcessor(IInvertedIndex index) {
        this.index = index;
    }

    /**
     * Takes a Context object from the KaVe datasets and create IndexDocument
     */
    public void processSST(Context ctx) {
        ISST sst = ctx.getSST();

        ISSTNodeVisitor indexDocumentExtractionVisitor = new IndexDocumentExtractionVisitorNoList();

        sst.accept(indexDocumentExtractionVisitor, index);
    }

    public void startProcessSSTs() {
        index.startIndexing();
    }

    public void finishProcessSSTs() {
        index.finishIndexing();
    }

}

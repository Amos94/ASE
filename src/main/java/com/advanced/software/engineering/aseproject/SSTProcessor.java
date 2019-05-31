package com.advanced.software.engineering.aseproject;

import index.IInvertedIndex;
import index.IndexDocumentExtractionVisitorNoList;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

class SSTProcessor {

    private IInvertedIndex index;

    /**
     * Create InvertedIndex
     */
    SSTProcessor(IInvertedIndex index) {
        this.index = index;
    }

    /**
     * Takes a context object from the KaVe datasets and create IndexDocument
     */
    void processSST(Context ctx, String projectName) {
        ISST sst = ctx.getSST();

        ISSTNodeVisitor indexDocumentExtractionVisitor = new IndexDocumentExtractionVisitorNoList(projectName);

        sst.accept(indexDocumentExtractionVisitor, index);
    }

    /**
     * Start to process SSTs
     */
    void startProcessSSTs() {
        index.startIndexing();
    }

    /**
     * Finish to process SSTs
     */
    void finishProcessSSTs() {
        index.finishIndexing();
    }

}

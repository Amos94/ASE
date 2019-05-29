package com.advanced.software.engineering.aseproject;

import Index.IInvertedIndex;
import Index.IndexDocumentExtractionVisitorNoList;
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
     * Takes a Context object from the KaVe datasets and create IndexDocument
     */
    void processSST(Context ctx, String projectName) {
        ISST sst = ctx.getSST();

        ISSTNodeVisitor indexDocumentExtractionVisitor = new IndexDocumentExtractionVisitorNoList(projectName);

        sst.accept(indexDocumentExtractionVisitor, index);
    }

    void startProcessSSTs() {
        index.startIndexing();
    }

    void finishProcessSSTs() {
        index.finishIndexing();
    }

}

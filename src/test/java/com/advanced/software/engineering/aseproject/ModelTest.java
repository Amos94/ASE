package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import Index.IndexDocumentExtractionVisitorNoList;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void processSST() {
        List<String> overallContext = new LinkedList<>();
        overallContext.add("Regex");
        overallContext.add("bool");
        overallContext.add("Build");
        overallContext.add("Event");
        String docID = "1";
        String methodCall = "testMethod1";
        String type = "java.util.StringJoiner";
        long overallContextSimhash = 30896187;
        IndexDocument doc = new IndexDocument(docID, methodCall, type, overallContext, overallContextSimhash);
//        ISST sst = ctx.getSST();
//        ISSTNodeVisitor indexDocumentExtractionVisitor = new IndexDocumentExtractionVisitorNoList();
//        sst.accept(indexDocumentExtractionVisitor, index);
    }

    @Test
    public void startProcessSSTs() {
    }

    @Test
    public void finishProcessSSTs() {
    }
}
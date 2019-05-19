package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
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
    }

    @Test
    public void startProcessSSTs() {
    }

    @Test
    public void finishProcessSSTs() {
    }
}
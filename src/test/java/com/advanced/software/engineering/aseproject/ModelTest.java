package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void processSST() {
        Collection<String> overallContext = new LinkedList<>();
        overallContext.add("Regex");
        overallContext.add("bool");
        overallContext.add("Build");
        overallContext.add("Event");
        String docID = "1";
        String methodCall = "testMethod1";
        String type = "java.util.StringJoiner";
        IndexDocument doc = new IndexDocument(docID, methodCall, type, overallContext);
    }
    @Test
    public void startProcessSSTs() {
    }

    @Test
    public void finishProcessSSTs() {
    }
}
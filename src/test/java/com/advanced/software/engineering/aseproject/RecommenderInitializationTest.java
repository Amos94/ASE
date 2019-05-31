package com.advanced.software.engineering.aseproject;

import context.ContextHelper;
import context.IoHelper;
import index.IInvertedIndex;
import index.InvertedIndex;
import utils.Configuration;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class RecommenderInitializationTest {

    @Test
    public void createIndex() {
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.TEST_CONTEXTS_DIR);
        Set<String> zips = IoHelper.findAllZips(Configuration.TEST_CONTEXTS_DIR);
        int numberOfZips = zips.size();
        assertNotEquals(0,numberOfZips);

        ContextHelper ctxHelper = new ContextHelper(Configuration.TEST_CONTEXTS_DIR);
        assertNotEquals(null, ctxHelper);

        long startTime = System.currentTimeMillis();
        assertNotEquals(null, startTime);

        IInvertedIndex invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        assertNotEquals(null, invertedIndex);

    }
}
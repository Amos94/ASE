package com.advanced.software.engineering.aseproject;

import context.ContextHelper;
import context.IoHelper;
import index.IInvertedIndex;
import index.InvertedIndex;
import utils.Configuration;
import helper.TestHelper;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class RecommenderInitializationTest {

    @Test
    public void createIndex() {
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(TestHelper.TEST_CONTEXTS_DIR);
        Set<String> zips = IoHelper.findAllZips(TestHelper.TEST_CONTEXTS_DIR);
        int numberOfZips = zips.size();
        assertNotEquals(0,numberOfZips);

        ContextHelper ctxHelper = new ContextHelper(TestHelper.TEST_CONTEXTS_DIR);
        assertNotEquals(null, ctxHelper);

        long startTime = System.currentTimeMillis();
        assertNotEquals(null, startTime);

        IInvertedIndex invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        assertNotEquals(null, invertedIndex);

    }
}
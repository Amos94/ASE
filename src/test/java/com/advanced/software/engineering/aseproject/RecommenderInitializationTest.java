package com.advanced.software.engineering.aseproject;

import Context.ContextHelper;
import Context.IoHelper;
import Index.IInvertedIndex;
import Index.InvertedIndex;
import Utils.Configuration;
import helper.TestHelper;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class RecommenderInitializationTest {

    @Test
    public void createIndex() {
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
package com.advanced.software.engineering.aseproject;

import Context.ContextHelper;
import Context.IoHelper;
import Index.IInvertedIndex;
import Index.InvertedIndex;
import Utils.Configuration;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class RecommenderInitializationTest {

    @Test
    public void createIndex() {
        Set<String> zips = IoHelper.findAllZips(Configuration.CONTEXTS_DIR);
        int numberOfZips = zips.size();
        assertNotEquals(0,numberOfZips);

        ContextHelper ctxHelper = new ContextHelper(Configuration.CONTEXTS_DIR);
        assertNotEquals(null, ctxHelper);

        long startTime = System.currentTimeMillis();
        assertNotEquals(null, startTime);

        IInvertedIndex invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        assertNotEquals(null, invertedIndex);

        Model model = new Model(invertedIndex);
        assertNotEquals(null, model);
    }
}
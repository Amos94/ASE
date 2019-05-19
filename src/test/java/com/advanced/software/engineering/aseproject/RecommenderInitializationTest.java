package com.advanced.software.engineering.aseproject;

import Context.ContextHelper;
import Context.IoHelper;
import Index.IInvertedIndex;
import Index.InvertedIndex;
import Utils.Configuration;
import org.junit.Test;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class RecommenderInitializationTest {

    @Test
    public void RecommenderInitialization(){
        assertEquals("com.advanced.software.engineering.aseproject.RecommenderInitialization", RecommenderInitialization.class.getName());
        Logger logger  = Logger.getLogger(RecommenderInitialization.class.getName());
        assertNotEquals(null,logger);
    }

    @Test
    public void createIndex() {
//      ctxHelper unused
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

    @Test
    public void queryIndex() {
//      this method seems incomplete
        Set<String> zips = IoHelper.findAllZips(Configuration.EVENTS_DIR);
        int numberOfZips = zips.size();
        assertNotEquals(0,numberOfZips);
    }
}
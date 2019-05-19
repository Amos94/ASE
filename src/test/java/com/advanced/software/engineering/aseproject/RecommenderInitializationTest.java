package com.advanced.software.engineering.aseproject;

import Context.IoHelper;
import Utils.Configuration;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class RecommenderInitializationTest {

    @Test
    public void createIndex() {
//      ctxHelper unused
        Set<String> zips = IoHelper.findAllZips(Configuration.CONTEXTS_DIR);
        int numberOfZips = zips.size();
        assertNotEquals(0,numberOfZips);
    }

    @Test
    public void queryIndex() {
//      this method seems incomplete
        Set<String> zips = IoHelper.findAllZips(Configuration.EVENTS_DIR);
        int numberOfZips = zips.size();
        assertNotEquals(0,numberOfZips);
    }
}
package com.advanced.software.engineering.aseproject;

import org.junit.Test;
import org.junit.Before;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.events.completionevents.Context;
import Index.IndexDocument;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

public class RecommenderTest {
	
	@Mock private Recommender recommender;
	@Mock private Context ctx;
	@Mock private IndexDocument query;
	@Mock private List<IName> ideProposals;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public void query() {
    	Set<Pair<IMemberName, Double>> result = new LinkedHashSet<>();
    	Set<Pair<IMemberName, Double>> expected = new LinkedHashSet<>();
    	result = recommender.query(query);
    	assertEquals(expected, result);
    }

    @Test
    public void query1() {
    	Set<Pair<IMemberName, Double>> result = new LinkedHashSet<>();
    	Set<Pair<IMemberName, Double>> expected = new LinkedHashSet<>();
    	result = recommender.query(ctx);
    	assertEquals(expected, result);
    }

    @Test
    public void getLastModelSize() {
    	assertNotNull(recommender.getLastModelSize());
    }

    @Test
    public void query2() {
    	Set<Pair<IMemberName, Double>> result = new LinkedHashSet<>();
    	Set<Pair<IMemberName, Double>> expected = new LinkedHashSet<>();
    	result = recommender.query(ctx, ideProposals);
    	assertEquals(expected, result);
    }
}
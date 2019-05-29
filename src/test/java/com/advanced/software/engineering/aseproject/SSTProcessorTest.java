package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import org.junit.Test;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.events.completionevents.Context;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SSTProcessorTest {
	
	@Mock private SSTProcessor processor;
	@Mock private Context ctx;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
    @Test
    public void processSST() {
		Collection<String> overallContext = new LinkedList<>();
		overallContext.add("Regex");
		overallContext.add("bool");
		overallContext.add("Build");
		overallContext.add("Event");
		String methodCall = "testMethod1";
		String type = "java.util.StringJoiner";
		String projectName = "Test-Project";
		IndexDocument doc = new IndexDocument(methodCall, null, type, overallContext,projectName);
    	processor.processSST(ctx,projectName);
    	verify(processor).processSST(ctx,projectName);
    	assertNotNull(doc);
    }

    @Test
    public void startProcessSSTs() {
    	processor.startProcessSSTs();
    	verify(processor).startProcessSSTs();
    }

    @Test
    public void finishProcessSSTs() {
    	processor.startProcessSSTs();
    	verify(processor).startProcessSSTs();
    }
}
package com.advanced.software.engineering.aseproject;

import org.junit.Test;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.events.completionevents.Context;

import static org.junit.Assert.*;
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
    	processor.processSST(ctx);
    	verify(processor).processSST(ctx);
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
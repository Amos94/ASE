package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import org.junit.Test;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.LinkedList;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

//	@Mock private Model model;

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
        String docID = "1";
        String methodCall = "testMethod1";
        String type = "java.util.StringJoiner";
        IndexDocument doc = new IndexDocument(docID, methodCall, type, overallContext);
    }
    @Test
    public void startProcessSSTs() {
//    	model.startProcessSSTs();
//    	verify(model).startProcessSSTs();
    }

    @Test
    public void finishProcessSSTs() {
//    	model.finishProcessSSTs();
//    	verify(model).finishProcessSSTs();
    }
}
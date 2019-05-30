package com.advanced.software.engineering.aseproject;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Index.IndexDocument;
import org.junit.Test;
import SimilarityMeasures.JaccardSimilarity;
import org.mockito.Mock;

public class EvaluatorTest {
    @Mock private IndexDocument queryDoc;
    @Mock private IndexDocument scoredDoc;

	private Set<String> mySet1 = new HashSet<>();
	private Set<String> mySet2 = new HashSet<>();
	private Set<String> mySet3 = new HashSet<>();
	private Set<String> mySet4 = new HashSet<>();

	@Test
    public void constructorTest(){
        Evaluator evaluator = new Evaluator(scoredDoc, queryDoc);
    }

    @Test
    public void calculateJaccard() {
        String[] a = new String[]{ "Hallo", "Bye", "Never", "No", "Class", "Java" };
        String[] b = new String[]{ "Hallo", "Bye", "Never", "No", "Class", "Java" };
        String[] c = new String[]{ "Hallo", "Bye", "Never"};
        String[] d = new String[]{ "Cold"};

        mySet1.addAll(Arrays.asList(a));
        mySet2.addAll(Arrays.asList(b));
        mySet3.addAll(Arrays.asList(c));
        mySet4.addAll(Arrays.asList(d));

        JaccardSimilarity result1 = new JaccardSimilarity(mySet1,mySet2);
        JaccardSimilarity result2 = new JaccardSimilarity(mySet1,mySet3);
        JaccardSimilarity result3 = new JaccardSimilarity(mySet1,mySet4);
        JaccardSimilarity result4 = new JaccardSimilarity(mySet3,mySet4);

        assertEquals(1, result1.computeSimilarity(), 0.001);
        assertEquals(0.5, result2.computeSimilarity(), 0.001);
        assertEquals(0, result3.computeSimilarity(), 0.001);
        assertEquals(0, result4.computeSimilarity(), 0.001);

    }

}
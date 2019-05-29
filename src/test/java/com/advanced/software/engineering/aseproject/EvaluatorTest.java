package com.advanced.software.engineering.aseproject;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import SimilarityMeasures.JaccardSimilarity;

public class EvaluatorTest {

	private Set<String> mySet1 = new HashSet<>();
	private Set<String> mySet2 = new HashSet<>();
	private Set<String> mySet3 = new HashSet<>();
	private Set<String> mySet4 = new HashSet<>();

	//@TODO add Test for Constructor

    @Test
    public void calculateJaccard() {
        String[] a = new String[]{ "Hallo", "Bye", "Never", "No", "Class", "Java" };
        String[] b = new String[]{ "Hallo", "Bye", "Never", "No", "Class", "Java" };
        String[] c = new String[]{ "Hallo", "Bye", "Never"};
        String[] d = new String[]{ "Cold"};

        for (String value : a) {
            mySet1.add(value);
        }
        for (String value : b) {
            mySet2.add(value);
        }
        for (String value : c) {
            mySet3.add(value);
        }
        for (String value : d) {
            mySet4.add(value);
        }

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
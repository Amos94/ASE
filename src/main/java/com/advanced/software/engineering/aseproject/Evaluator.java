package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import SimilarityMeasures.JaccardSimilarity;

import java.util.HashSet;
import java.util.Set;

public class Evaluator {

    private IndexDocument queryDoc;
    private IndexDocument scoredDoc;

    public Evaluator(IndexDocument storedDoc, IndexDocument queryDoc){
        this.scoredDoc = storedDoc;
        this.queryDoc = queryDoc;
    }

    public double calculateJaccard(){
        Set<String> storedDocSet = new HashSet<>();
        Set<String> queryDocSet = new HashSet<>();

        storedDocSet.addAll(scoredDoc.getOverallContext());
        queryDocSet.addAll(queryDoc.getOverallContext());

        JaccardSimilarity result = new JaccardSimilarity(storedDocSet,queryDocSet);

        return result.computeSimilarity();
    }
}
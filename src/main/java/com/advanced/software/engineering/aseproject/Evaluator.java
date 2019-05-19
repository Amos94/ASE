package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import SimilarityMeasures.JaccardSimilarity;

import java.util.HashSet;
import java.util.Set;

/**
 * The Evaluator class which compares the queried doc with the scored doc
 *
 */
public class Evaluator {

    private IndexDocument queryDoc;
    private IndexDocument scoredDoc;

    /**
     * Initiate the Evaluator
     *
     * @param storedDoc
     * @param queryDoc
     */
    public Evaluator(IndexDocument storedDoc, IndexDocument queryDoc){
        this.scoredDoc = storedDoc;
        this.queryDoc = queryDoc;
    }

    /**
     * Calculate the Jaccard Similarity by creating hashSets of both documents
     *
     * @return double computedSimilarity
     */
    public double calculateJaccard(){
        Set<String> storedDocSet = new HashSet<>();
        Set<String> queryDocSet = new HashSet<>();

        storedDocSet.addAll(scoredDoc.getOverallContext());
        queryDocSet.addAll(queryDoc.getOverallContext());

        JaccardSimilarity result = new JaccardSimilarity(storedDocSet,queryDocSet);

        return result.computeSimilarity();
    }
}
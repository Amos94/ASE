package com.advanced.software.engineering.aseproject;

import Index.IndexDocument;
import SimilarityMeasures.JaccardSimilarity;

import java.util.HashSet;
import java.util.Set;

/**
 * The Evaluator class which compares the queried doc with the scored doc
 *
 */
class Evaluator {

    private IndexDocument queryDoc;
    private IndexDocument scoredDoc;

    /**
     * Initiate the Evaluator
     *
     * @param storedDoc - stored document
     * @param queryDoc - query document
     */
    Evaluator(IndexDocument storedDoc, IndexDocument queryDoc){
        this.scoredDoc = storedDoc;
        this.queryDoc = queryDoc;
    }

    /**
     * Calculate the Jaccard Similarity by creating hashSets of both documents
     *
     * @return double computedSimilarity
     */
    double calculateJaccard(){

        Set<String> storedDocSet = new HashSet<>(scoredDoc.getOverallContext());
        Set<String> queryDocSet = new HashSet<>(queryDoc.getOverallContext());

        JaccardSimilarity result = new JaccardSimilarity(storedDocSet,queryDocSet);

        return result.computeSimilarity();
    }
}
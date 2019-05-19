package SimilarityMeasures;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class JaccardSimilarityTest {

    @Test
    public void computeSimilarity() {
        int[] a = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
        int[] b = new int[]{ 1,2,3,4,5,6 };

        Set<Integer> indexSet = new HashSet<>();
        for (int value : a) {
            indexSet.add(value);
        }
        Set<Integer> querySet = new HashSet<>();
        for (int value : b) {
            querySet.add(value);
        }

        Set<Integer> intersection = new HashSet<>(indexSet);
        intersection.retainAll(querySet);

        Set<Integer> union = new HashSet<>(indexSet);
        union.addAll(querySet);

        assertEquals((double) b.length/a.length,(double) intersection.size() / (double) union.size(), 0);

    }
}
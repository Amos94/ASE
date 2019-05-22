package SimilarityMeasures;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class JaccardSimilarityTest {

    @Test
    public void computeSimilarity() {
        String[] a = new String[]{"file", "input", "read"};
        String[] b = new String[]{"file", "write"};

        Set<String> indexSet = new HashSet<>();
        Collections.addAll(indexSet, a);
        Set<String> querySet = new HashSet<>();
        Collections.addAll(querySet, b);

        Set<String> intersection = new HashSet<>(indexSet);
        intersection.retainAll(querySet);
        assertEquals("[file]",intersection.toString());

        Set<String> union = new HashSet<>(indexSet);
        union.addAll(querySet);
        assertEquals("[file, input, read, write]",union.toString());

        JaccardSimilarity jSim = new JaccardSimilarity(indexSet, querySet);
        assertEquals(0.25,jSim.computeSimilarity(), 0);
    }
}
package SimilarityMeasures;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;

public class CosineSimilarityTest {

    @Test
    public void cosineSimilarity() {
        CosineSimilarity cosSim = new CosineSimilarity();
        final Map<CharSequence, Integer> leftVector  = new HashMap<>();
        final Map<CharSequence, Integer> rightVector = new HashMap<>();
        leftVector.put("aa",1);
        rightVector.put("aa",1);

        final Set<CharSequence> intersection = cosSim.getIntersection(leftVector, rightVector);
        assertEquals("[aa]",intersection.toString());

        final double dotProduct = cosSim.dot(leftVector, rightVector, intersection);
        assertEquals(1.0,dotProduct,0);

        Double result = cosSim.cosineSimilarity(leftVector, rightVector);
        assertEquals(1.0,result,0);

    }

}
package similarity_measures;

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
        leftVector.put("file",1);
        leftVector.put("input",2);
        leftVector.put("read",3);
        rightVector.put("file",1);
        rightVector.put("write",2);

        final Set<CharSequence> intersection = cosSim.getIntersection(leftVector, rightVector);
        assertEquals("[file]",intersection.toString());

        final double dotProduct = cosSim.dot(leftVector, rightVector, intersection);
        assertEquals(1.0,dotProduct,0);

        double result = cosSim.cosineSimilarity(leftVector, rightVector);
        assertEquals(0.119,result,0.001);

    }

}
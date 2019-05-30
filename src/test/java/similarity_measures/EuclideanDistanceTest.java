package similarity_measures;

import org.junit.Test;

import static similarity_measures.EuclideanDistance.calculateEuclideanDistance;
import static org.junit.Assert.*;

public class EuclideanDistanceTest {

    @Test
    public void calculateEuclideanDistanceTest() {
        double[] a = new double[]{ 0,0};
        double[] b = new double[]{ 4,3};
        double result = calculateEuclideanDistance(a,b);
        assertEquals(5.0,result,0);
    }
}
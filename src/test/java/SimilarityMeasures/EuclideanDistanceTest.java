package SimilarityMeasures;

import org.junit.Test;

import static org.junit.Assert.*;

public class EuclideanDistanceTest {

    @Test
    public void calculateEuclideanDistanceTest() {
        double[] a = new double[]{ 0,0};
        double[] b = new double[]{ 4,3};
        EuclideanDistance euclidDist = new EuclideanDistance(a,b);
        double result = euclidDist.calculateEuclideanDistance(a,b);
        assertEquals(5.0,result,0);
    }
}
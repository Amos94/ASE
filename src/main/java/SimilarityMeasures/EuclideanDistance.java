package SimilarityMeasures;

public class EuclideanDistance {

    /**
     * Additional Constructor
     *
     * @param array1
     * @param array2
     */
    public EuclideanDistance(double[] array1, double[] array2){
        calculateEuclideanDistance(array1, array2);
    }
//this constructor does not return or set any values
    /**
     * Get the euclidean distance out of two arrays
     * @param array1
     * @param array2
     * @return
     */
    public static double calculateEuclideanDistance(double[] array1, double[] array2)
    {
        assert(array1.length <= array2.length);
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
            Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
    }
}

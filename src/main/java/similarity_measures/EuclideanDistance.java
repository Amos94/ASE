package similarity_measures;

public class EuclideanDistance {

    /**
     * Additional Constructor
     *
     * @param array1 - array 1
     * @param array2 - array 2
     */
    public EuclideanDistance(double[] array1, double[] array2){
        calculateEuclideanDistance(array1, array2);
    }

    /**
     * Get the euclidean distance out of two arrays
     * @param array1 - array 1
     * @param array2 - array 2
     * @return - euclidean distance
     */
    static double calculateEuclideanDistance(double[] array1, double[] array2)
    {
        assert(array1.length <= array2.length);
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
            Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
    }
}

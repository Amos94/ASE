package similarity_measures;

import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarity {

    private Set indexSet;
    private Set querySet;

    /**
     * @param indexSet - The set retrieved from the db
     * @param querySet - current query set
     */
    public JaccardSimilarity(Set indexSet, Set querySet){
        this.indexSet = indexSet;
        this.querySet = querySet;
    }

    /**
     * @return (double) Jaccard similarity of two sets
     */
    public double computeSimilarity() {
        Set intersection = new HashSet(this.indexSet);
        intersection.retainAll(this.querySet);

        Set union = new HashSet(this.indexSet);
        union.addAll(this.querySet);

        return ((double) intersection.size() / (double) union.size());
    }

}

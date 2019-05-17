package Index;

/**
 * Interface for an index that can be used to index the documents needed for the model
 */
public interface IInvertedIndex {

    /**
     * Puts an IndexDocument in the index.
     */
    void indexDocument(IndexDocument doc);


    /**
     * Call this before using processDocument
     */
    void startIndexing();

    /**
     * Call this when you're done with indexing.
     */
    void finishIndexing();


}

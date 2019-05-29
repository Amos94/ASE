package Index;

import java.util.List;

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

    /**
     *
     *  get all documents from db
     */
    List<IndexDocument> deserializeAll();

    //get data by project
    List<IndexDocument> deserializeByProject(String projectName);


}

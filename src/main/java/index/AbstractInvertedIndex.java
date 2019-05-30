package index;

import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class implementing {@link IInvertedIndex}
 * Indexes documents using Apache Lucene's {@link IndexWriter}
 */
public abstract class AbstractInvertedIndex implements IInvertedIndex {

    private static final Logger LOGGER = Logger.getLogger( InvertedIndex.class.getName() );

    private Directory indexDirectory;
    private IndexWriter indexWriter;

    /**
     * Method to initialize the directories
     */
    private void initializeDirectory() {
        try {
            indexDirectory = getIndexDirectory();
        } catch (LockObtainFailedException e) {
            LOGGER.log(Level.SEVERE, "Error while executing the search (LockObtainFailedException) ", e);
            System.exit(1); // can't write to indexDirectory, abort
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while initalizing Directory ", e);
        }
    }

    /**
     * Start to create an indexed document
     *
     * @param doc the document to be indexed
     */
    public void indexDocument(IndexDocument doc) {
        if (isIndexed(doc)) {
            return;
        }
        try {
            serializeIndexDocument(doc);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while indexing the document ", e);
            System.exit(1);
        }
    }

    /**
     * Checks if a document is already in the index.
     */
    abstract boolean isIndexed(IndexDocument doc);

    /**
     * Serialize a given document
     */
    abstract void serializeIndexDocument(IndexDocument doc) throws IOException;

    /**
     * Get the directory of this document.
     * */
    abstract Directory getIndexDirectory() throws IOException;

    /**
     * deserialize IndexDocument object with the given docID
     */
    abstract IndexDocument deserializeIndexDocument(String docID) throws IOException;

    /**
     * Method to start the indexWriting and instantiate the indexWriter properly
     */
    @Override
    public void startIndexing() {
        initializeDirectory();
        IndexWriterConfig config = new IndexWriterConfig();
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            indexWriter = new IndexWriter(indexDirectory, config);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while starting to index ", e);
        }
    }

    /**
     * Method to close the indexWriter properly
     */
    @Override
    public void finishIndexing() {
        try {
            indexWriter.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while finishing the index ", e);
        }
    }

}

package Index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class implementing {@link IInvertedIndex}
 * Indexes documents using Apache Lucene's {@link IndexWriter}
 */
public abstract class AbstractInvertedIndex implements IInvertedIndex {

    // fields for indexing in Lucene index
    private static final String DOC_ID_FIELD = "docID";
    private static final String OVERALL_CONTEXT_FIELD = "overallContext";
    private static final String TYPE_FIELD = "type";
    private StringField docIdField = new StringField(DOC_ID_FIELD, "", Field.Store.YES);
    private StringField typeField = new StringField(TYPE_FIELD, "", Field.Store.NO);

    private Directory indexDirectory;
    private IndexWriter indexWriter;
    private IndexSearcher searcher;

    /**
     * Method to initialize the directories
     */
    private void initializeDirectory() {
        try {
            indexDirectory = getIndexDirectory();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
            System.exit(1); // can't write to indexDirectory, abort
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
     * Searches the Lucene index for documents which match doc's type and which contain similar terms in the overall
     * context as doc.
     * The query equals a boolean OR query of all terms in the overall context of doc.
     */
    public Set<IndexDocument> search(IndexDocument doc) {
        Set<IndexDocument> answers = new HashSet<>();
        try {
            BooleanQuery.Builder boolQueryBuilder = new BooleanQuery.Builder();
            boolQueryBuilder.setMinimumNumberShouldMatch(1);
            Query queryForType = new TermQuery(new Term(TYPE_FIELD, doc.getType()));
            boolQueryBuilder.add(queryForType, BooleanClause.Occur.MUST);
            for (String termStr : doc.getOverallContext()) {
                Term term = new Term(OVERALL_CONTEXT_FIELD, termStr);
                Query queryForOverallContext = new TermQuery(term);
                boolQueryBuilder.add(queryForOverallContext, BooleanClause.Occur.SHOULD);
            }
            Query boolQuery = boolQueryBuilder.build();
            List<Integer> docs = new ArrayList<>();
            Collector collector = new Collector() {
                @Override
                public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
                    return new LeafCollector() {
                        @Override
                        public void setScorer(Scorer scorer) throws IOException {

                        }
                        @Override
                        public void collect(int doc) throws IOException {
                            docs.add(doc);
                        }
                    };
                }
                @Override
                public boolean needsScores() {
                    return false;
                }
            };
            searcher.search(boolQuery, collector);
            for (Integer luceneDocID : docs) {
                Document luceneDoc = searcher.doc(luceneDocID);
                String docID = luceneDoc.get(DOC_ID_FIELD);
//                System.out.println(docID);
                IndexDocument matchingDoc = deserializeIndexDocument(docID);
                answers.add(matchingDoc);
            }
        } catch (IndexNotFoundException e) {
            e.printStackTrace();
            System.exit(1); // exit on IOException
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // exit on IOException
        }
        return answers;
    }

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

}

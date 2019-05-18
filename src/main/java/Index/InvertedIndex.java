package Index;

import Utils.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class InvertedIndex extends AbstractInvertedIndex {

    /*
      CLASS & INSTANCE VARIABLES
     */

    private static final String SQL_TABLE_NAME = "indexdocuments";
    private static final String INDEX_ROOT_DIR_NAME = "IndexStorage";
    private static final String SERIALIZED_INDEX_DOCUMENTS_DIR_NAME = "IndexDocuments";
    private static final String SERIALIZED_INDEX_DOCUMENTS_SQLITE_FILE_NAME = "IndexDocuments.db";
    private static final String INVERTED_INDEX_STRUCTURES_DIR_NAME = "InvertedIndexStructures_Lucene";
    private final Logger LOGGER = Logger.getLogger(InvertedIndex.class.getName());
    // directory where the Lucene index is persisted on disk
    private String indexRootDir;

    // connection to SQLite database
    private Connection dbConn;

    // true: we store IndexDocuments in SQLite database
    // false: we serialize IndexDocuments to disk as files with .ser ending
    private boolean USE_SQLITE = true;

    /*
      CONSTRUCTOR METHODS
     */

    /**
     * Create InvertedIndex
     * Uses an SQLite database to store the IndexDocument objects.
     */
    public InvertedIndex(String indexDir) {
        this(indexDir, true);
    }

    /**
     * Create InvertedIndex
     * Uses an SQLite database to store the IndexDocument objects.
     */
    public InvertedIndex(String indexDir, boolean useRelationalDatabase) {
        indexRootDir = indexDir + "/" + INDEX_ROOT_DIR_NAME;
        createDirectoryIfNotExists(new File(indexRootDir));
        this.USE_SQLITE = useRelationalDatabase;
    }

    private void createDBSchemaIfNotExists(Connection sqlConnection) throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + this.SQL_TABLE_NAME
                + "("
                + "   docid                  CHAR(64) PRIMARY KEY,"
                + "   type                   VARCHAR(1) NOT NULL," // SQLite does not enforce length of VARCHAR
                + "   method                 VARCHAR(1) NOT NULL,"
                + "   overallcontext         VARCHAR(1),"
                + "   overallcontextsimhash  BIGINT"
                + ")";
        Statement stmt = sqlConnection.createStatement();
        stmt.execute(sqlCreate);
        stmt.close();
    }

    @Override
    public void startIndexing() {
        if(Configuration.REINDEX_DATABASE == true) {
            super.startIndexing();
            if (USE_SQLITE) {
                openSQLConnection();
                try {
                    createDBSchemaIfNotExists(dbConn);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

    }

    private void openSQLConnection() {
        String sqlUrl = "jdbc:sqlite:" + indexRootDir + "/" + SERIALIZED_INDEX_DOCUMENTS_SQLITE_FILE_NAME;
        try {
            dbConn = DriverManager.getConnection(sqlUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1); // exit on exception
        }
    }

    @Override
    public void finishIndexing() {
        super.finishIndexing();
        if (USE_SQLITE && dbConn != null) {
            try {
                dbConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
      AbstractInvertedIndex IMPLEMENTATIONS
     */

    @Override
    boolean isIndexed(IndexDocument doc) {
        if (USE_SQLITE) {
            return isIndexedInDB(doc);
        } else {
            return isIndexedAsFile(doc);
        }
    }

    /**
     * Check if index is in the db
     *
     * @param doc
     * @return
     */
    private boolean isIndexedInDB(IndexDocument doc) {
        String sqlSelect = "SELECT docid FROM " + this.SQL_TABLE_NAME + " WHERE docid=\"" + doc.getId() + "\"";
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlSelect);
            boolean hasItems = rs.isBeforeFirst();
            rs.close();
            stmt.close();
            return hasItems;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if indexed as file
     *
     * @param doc
     * @return
     */
    private boolean isIndexedAsFile(IndexDocument doc) {
        File f = new File(getPathToFileForIndexDocument(doc.getId()));
        return f.exists();
    }

    /**
     * Gets the path to file for the IndexDocument
     *
     * @param docID
     * @return
     */
    private String getPathToFileForIndexDocument(String docID) {
        return indexRootDir + "/" + SERIALIZED_INDEX_DOCUMENTS_DIR_NAME + "/" + docID + ".ser";
    }

    @Override
    void serializeIndexDocument(IndexDocument doc) throws IOException {
        if (USE_SQLITE) {
            try {
                serializeToSQLite(doc);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage()); // TODO: it's probably not best practise to turn an SQLException into an IOException
            }
        } else {
            serializeToFile(doc);
        }
    }

    /**
     * Serialize to SQLite
     *
     * @param doc
     * @throws SQLException
     */
    private void serializeToSQLite(IndexDocument doc) throws SQLException {
        String sqlInsert = "INSERT INTO " + this.SQL_TABLE_NAME + " VALUES(?,?,?,?,?)";
        PreparedStatement prepStmt = dbConn.prepareStatement(sqlInsert);
        prepStmt.setString(1, doc.getId());
        prepStmt.setString(2, doc.getType());
        prepStmt.setString(3, doc.getMethodCall());
        prepStmt.setString(4, serializeContext(doc.getOverallContext()));
        prepStmt.setLong(5, doc.getOverallContextSimhash());
        prepStmt.executeUpdate();
        prepStmt.close();
    }

    /**
     * Serialize to file
     *
     * @param doc
     * @throws IOException
     */
    private void serializeToFile(IndexDocument doc) throws IOException {
        String contextsDirPath = indexRootDir + "/" + SERIALIZED_INDEX_DOCUMENTS_DIR_NAME;
        createDirectoryIfNotExists(new File(contextsDirPath));
        FileOutputStream fileOut = new FileOutputStream(getPathToFileForIndexDocument(doc.getId()));
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(doc);
        out.close();
        fileOut.close();
    }

    @Override
    Directory getIndexDirectory() throws IOException {
        String luceneIndexDirPath = indexRootDir + "/" + INVERTED_INDEX_STRUCTURES_DIR_NAME;
        FSDirectory fileDirectory = FSDirectory.open(new File(luceneIndexDirPath).toPath());
        return fileDirectory;
    }

    @Override
    IndexDocument deserializeIndexDocument(String docID) throws IOException {
        if (USE_SQLITE) {
            return deserializeFromSQLite(docID);
        } else {
            return deserializeFromFile(docID);
        }
    }

    /**
     * Deserialize from SQLite
     *
     * @param docID
     * @return
     */
    private IndexDocument deserializeFromSQLite(String docID) {
        String sqlSelect = "SELECT * FROM " + this.SQL_TABLE_NAME + " WHERE docid=\"" + docID + "\"";
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlSelect);
            boolean hasItems = rs.isBeforeFirst();
            if (hasItems) {
                String methodCall = rs.getString("method");
                String type = rs.getString("type");
                List<String> overallContext = deserializeContext(rs.getString("overallcontext"));
                long overallContextSimhash = rs.getLong("overallcontextsimhash");
                IndexDocument doc = new IndexDocument(docID, methodCall, type, overallContext, overallContextSimhash);
                return doc;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Serialize the context
     *
     * @param context
     * @return
     */
    private String serializeContext(List<String> context) {
        StringBuilder sb = new StringBuilder();
        for (String s : context) {
            sb.append(s.length());
            sb.append("~");
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Deserialize the Context
     *
     * @param context
     * @return
     */
    private List<String> deserializeContext(String context) {
        List<String> result = new LinkedList<>();
        int position = 0;
        while (position < context.length()) {
            int tildePosition = position + context.substring(position).indexOf("~");
            int wordLength = Integer.valueOf(context.substring(position, tildePosition));
            String s = context.substring(tildePosition + 1, tildePosition + 1 + wordLength);
            result.add(s);
            position = tildePosition + wordLength + 1;
        }
        return result;
    }

    /**
     * Deserialization from File
     *
     * @param docID
     * @return
     * @throws IOException
     */
    private IndexDocument deserializeFromFile(String docID) throws IOException {
        IndexDocument doc = null;
        FileInputStream fileIn = new FileInputStream(getPathToFileForIndexDocument(docID));
        ObjectInputStream in = new ObjectInputStream(fileIn);
        try {
            doc = (IndexDocument) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1); // exit on exception
        }
        in.close();
        fileIn.close();
        return doc;
    }


    /**
     * Create a directory for the output if nothing exists already
     *
     * @param dir
     */
    private void createDirectoryIfNotExists(File dir) {
        if (!dir.exists()) {
            System.out.println("'" + dir + "' does not exist yet. Creating it... ");
            try {
                FileUtils.forceMkdir(dir);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1); // exit on IOException
            }
        }
    }

}

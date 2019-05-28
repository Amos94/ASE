package Index;


import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.*;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class InvertedIndex extends AbstractInvertedIndex {

    // CLASS & INSTANCE VARIABLES
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

    /**
     * Gets the path to file for the IndexDocument
     *
     * @param docID
     * @return
     */
    private String getPathToFileForIndexDocument(String docID) {
        return indexRootDir + "/" + SERIALIZED_INDEX_DOCUMENTS_DIR_NAME + "/" + docID + ".ser";
    }

    /**
     * Method to get the IndexDirectory
     *
     * @return
     * @throws IOException
     */
    @Override
    Directory getIndexDirectory() throws IOException {
        String luceneIndexDirPath = indexRootDir + "/" + INVERTED_INDEX_STRUCTURES_DIR_NAME;
        FSDirectory fileDirectory = FSDirectory.open(new File(luceneIndexDirPath).toPath());
        return fileDirectory;
    }


    /**
     * Create InvertedIndex
     * Uses an SQLite database to store the IndexDocument objects.
     *
     * @param indexDir
     */
    public InvertedIndex(String indexDir) {
        this(indexDir, true);
    }

    /**
     * Create InvertedIndex
     * Uses an SQLite database to store the IndexDocument objects.
     *
     * @param indexDir
     * @param useRelationalDatabase
     */
    public InvertedIndex(String indexDir, boolean useRelationalDatabase) {
        indexRootDir = indexDir + "/" + INDEX_ROOT_DIR_NAME;
        createDirectoryIfNotExists(new File(indexRootDir));
        this.USE_SQLITE = useRelationalDatabase;
    }

    /**
     * Creates the basic schema if it is not already there
     *
     * @param sqlConnection
     * @throws SQLException
     */
    private void createDBSchemaIfNotExists(Connection sqlConnection) throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + this.SQL_TABLE_NAME
                + "("
                + "docid                  CHAR(64) PRIMARY KEY,"
                + "declaringtype          VARCHAR(1) NOT NULL,"
                + "valuetype              VARCHAR(1) NOT NULL,"
                + "isstatic               SMALLINT   NOT NULL,"
                + "methodname             VARCHAR(1) NOT NULL,"
                + "methodfullname         VARCHAR(1) NOT NULL,"
                + "identifier             VARCHAR(1) NOT NULL,"
                + "isunknown              SMALLINT   NOT NULL,"
                + "ishashed               SMALLINT   NOT NULL,"
                + "overallcontext         VARCHAR(1),"
                + "projectname            VARCHAR(1)"
                + ")";

        try (Statement stmt = sqlConnection.createStatement()) {
            stmt.execute(sqlCreate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    /**
     * Method to start indexing
     */
    @Override
    public void startIndexing() {
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

    /**
     * Method to open the sql connection via sqlite
     */
    private void openSQLConnection() {
        String sqlUrl = "jdbc:sqlite:" + indexRootDir + "/" + SERIALIZED_INDEX_DOCUMENTS_SQLITE_FILE_NAME;
        try {
            dbConn = DriverManager.getConnection(sqlUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1); // exit on exception
        }
    }

    /**
     * Method to distinguish between indexing in file and in db
     *
     * @param doc
     * @return
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
        try (Statement stmt = dbConn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sqlSelect)) {
                boolean hasItems = rs.isBeforeFirst();
                return hasItems;
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
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
     * Serializes the content to a document
     *
     * @param doc
     * @throws IOException
     */
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
     * Serialize to SQLite with a prepared statement.
     *
     * @param doc
     * @throws SQLException
     */
    private void serializeToSQLite(IndexDocument doc) throws SQLException {
        String sqlInsert = "INSERT INTO " + this.SQL_TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlInsert)) {
            prepStmt.setString(1, doc.getId());
            prepStmt.setString(2, doc.getMethod().getDeclaringType().getName());
            prepStmt.setString(3, doc.getMethod().getValueType().getName());
            prepStmt.setInt(4, booleanToInt(doc.getMethod().isStatic()));
            prepStmt.setString(5, doc.getMethod().getName());
            prepStmt.setString(6, doc.getMethod().getFullName());
            prepStmt.setString(7, doc.getMethod().getIdentifier());
            prepStmt.setInt(8, booleanToInt(doc.getMethod().isUnknown()));
            prepStmt.setInt(9, booleanToInt(doc.getMethod().isHashed()));
            prepStmt.setString(10, splitContext(doc.getOverallContext()));
            prepStmt.setString(11, doc.getProjectName());
            prepStmt.executeUpdate();
        }
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
        try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(doc);
        }
        fileOut.close();
    }


    /**
     * Method to deserialize the indexed Document
     *
     * @param docID
     * @return
     * @throws IOException
     */
    @Override
    IndexDocument deserializeIndexDocument(String docID) throws IOException {
        if (USE_SQLITE) {
            return deserializeFromSQLite(docID);
        } else {
            return deserializeFromFile(docID);
        }
    }


    /**
     * Method to deserialze everything
     *
     * @return
     */
    @Override
    public List<IndexDocument> deserializeAll() {
        List<IndexDocument> documents = new LinkedList<>();
        String sqlSelect = "SELECT * FROM " + this.SQL_TABLE_NAME;
        openSQLConnection();
        try (Statement stmt = dbConn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sqlSelect)) {
                boolean hasItems = rs.isBeforeFirst();
                while (!rs.isAfterLast()) {

                    String docID = rs.getString("docid");
                    String methodName = rs.getString("methodname");
                    String methodFullName = rs.getString("methodfullname");
                    String declaringTypeName = rs.getString("declaringtype");
                    String valueTypeName = rs.getString("valuetype");

                    boolean isStatic = intToBool(rs.getInt("isstatic"));
                    boolean isUnknown = intToBool(rs.getInt("isunknown"));
                    boolean isHashed = intToBool(rs.getInt("ishashed"));
                    String identifier = rs.getString("identifier");

                    IMemberName method = new IMemberName() {
                        @Override
                        public ITypeName getDeclaringType() {
                            return new ITypeName() {
                                @Override
                                public int compareTo(ITypeName o) {
                                    return 0;
                                }

                                @Override
                                public String getIdentifier() {
                                    return null;
                                }

                                @Override
                                public boolean isUnknown() {
                                    return false;
                                }

                                @Override
                                public boolean isHashed() {
                                    return false;
                                }

                                @Override
                                public boolean hasTypeParameters() {
                                    return false;
                                }

                                @Override
                                public List<ITypeParameterName> getTypeParameters() {
                                    return null;
                                }

                                @Override
                                public IAssemblyName getAssembly() {
                                    return null;
                                }

                                @Override
                                public INamespaceName getNamespace() {
                                    return null;
                                }

                                @Override
                                public String getFullName() {
                                    return declaringTypeName;
                                }

                                @Override
                                public String getName() {
                                    return null;
                                }

                                @Override
                                public boolean isVoidType() {
                                    return false;
                                }

                                @Override
                                public boolean isValueType() {
                                    return false;
                                }

                                @Override
                                public boolean isSimpleType() {
                                    return false;
                                }

                                @Override
                                public boolean isEnumType() {
                                    return false;
                                }

                                @Override
                                public boolean isStructType() {
                                    return false;
                                }

                                @Override
                                public boolean isNullableType() {
                                    return false;
                                }

                                @Override
                                public boolean isReferenceType() {
                                    return false;
                                }

                                @Override
                                public boolean isClassType() {
                                    return false;
                                }

                                @Override
                                public boolean isInterfaceType() {
                                    return false;
                                }

                                @Override
                                public boolean isNestedType() {
                                    return false;
                                }

                                @Override
                                public ITypeName getDeclaringType() {
                                    return null;
                                }

                                @Override
                                public boolean isDelegateType() {
                                    return false;
                                }

                                @Override
                                public IDelegateTypeName asDelegateTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isArray() {
                                    return false;
                                }

                                @Override
                                public IArrayTypeName asArrayTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isTypeParameter() {
                                    return false;
                                }

                                @Override
                                public ITypeParameterName asTypeParameterName() {
                                    return null;
                                }

                                @Override
                                public boolean isPredefined() {
                                    return false;
                                }

                                @Override
                                public IPredefinedTypeName asPredefinedTypeName() {
                                    return null;
                                }
                            };
                        }

                        @Override
                        public ITypeName getValueType() {
                            return new ITypeName() {
                                @Override
                                public IAssemblyName getAssembly() {
                                    return null;
                                }

                                @Override
                                public INamespaceName getNamespace() {
                                    return null;
                                }

                                @Override
                                public String getFullName() {
                                    return null;
                                }

                                @Override
                                public String getName() {
                                    return valueTypeName;
                                }

                                @Override
                                public boolean isVoidType() {
                                    return false;
                                }

                                @Override
                                public boolean isValueType() {
                                    return false;
                                }

                                @Override
                                public boolean isSimpleType() {
                                    return false;
                                }

                                @Override
                                public boolean isEnumType() {
                                    return false;
                                }

                                @Override
                                public boolean isStructType() {
                                    return false;
                                }

                                @Override
                                public boolean isNullableType() {
                                    return false;
                                }

                                @Override
                                public boolean isReferenceType() {
                                    return false;
                                }

                                @Override
                                public boolean isClassType() {
                                    return false;
                                }

                                @Override
                                public boolean isInterfaceType() {
                                    return false;
                                }

                                @Override
                                public boolean isNestedType() {
                                    return false;
                                }

                                @Override
                                public ITypeName getDeclaringType() {
                                    return null;
                                }

                                @Override
                                public boolean isDelegateType() {
                                    return false;
                                }

                                @Override
                                public IDelegateTypeName asDelegateTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isArray() {
                                    return false;
                                }

                                @Override
                                public IArrayTypeName asArrayTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isTypeParameter() {
                                    return false;
                                }

                                @Override
                                public ITypeParameterName asTypeParameterName() {
                                    return null;
                                }

                                @Override
                                public boolean isPredefined() {
                                    return false;
                                }

                                @Override
                                public IPredefinedTypeName asPredefinedTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean hasTypeParameters() {
                                    return false;
                                }

                                @Override
                                public List<ITypeParameterName> getTypeParameters() {
                                    return null;
                                }

                                @Override
                                public String getIdentifier() {
                                    return null;
                                }

                                @Override
                                public boolean isUnknown() {
                                    return false;
                                }

                                @Override
                                public boolean isHashed() {
                                    return false;
                                }

                                @Override
                                public int compareTo(ITypeName o) {
                                    return 0;
                                }
                            };
                        }

                        @Override
                        public boolean isStatic() {
                            return isStatic;
                        }

                        @Override
                        public String getName() {
                            return methodName;
                        }

                        @Override
                        public String getFullName() {
                            return methodFullName;
                        }

                        @Override
                        public String getIdentifier() {
                            return identifier;
                        }

                        @Override
                        public boolean isUnknown() {
                            return isUnknown;
                        }

                        @Override
                        public boolean isHashed() {
                            return isHashed;
                        }
                    };
                    List<String> overallContext = deserializeContext(rs.getString("overallcontext"));
                    IndexDocument doc = new IndexDocument(docID, method, overallContext);
                    documents.add(doc);
                    rs.next();
                }
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    @Override
    public List<IndexDocument> deserializeByProject(String projectName) {
        List<IndexDocument> documents = new LinkedList<>();
        String sqlSelect = "SELECT * FROM " + this.SQL_TABLE_NAME + " WHERE projectName=\"" + projectName + "\"";
        openSQLConnection();
        try (Statement stmt = dbConn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sqlSelect)) {
                boolean hasItems = rs.isBeforeFirst();
                while (!rs.isAfterLast()) {

                    String docID = rs.getString("docid");
                    String methodName = rs.getString("methodname");
                    String methodFullName = rs.getString("methodfullname");
                    String declaringTypeName = rs.getString("declaringtype");
                    String valueTypeName = rs.getString("valuetype");

                    boolean isStatic = intToBool(rs.getInt("isstatic"));
                    boolean isUnknown = intToBool(rs.getInt("isunknown"));
                    boolean isHashed = intToBool(rs.getInt("ishashed"));
                    String identifier = rs.getString("identifier");
                    String projectname = rs.getString("projectName");

                    IMemberName method = new IMemberName() {
                        @Override
                        public ITypeName getDeclaringType() {
                            return new ITypeName() {
                                @Override
                                public int compareTo(ITypeName o) {
                                    return 0;
                                }

                                @Override
                                public String getIdentifier() {
                                    return null;
                                }

                                @Override
                                public boolean isUnknown() {
                                    return false;
                                }

                                @Override
                                public boolean isHashed() {
                                    return false;
                                }

                                @Override
                                public boolean hasTypeParameters() {
                                    return false;
                                }

                                @Override
                                public List<ITypeParameterName> getTypeParameters() {
                                    return null;
                                }

                                @Override
                                public IAssemblyName getAssembly() {
                                    return null;
                                }

                                @Override
                                public INamespaceName getNamespace() {
                                    return null;
                                }

                                @Override
                                public String getFullName() {
                                    return declaringTypeName;
                                }

                                @Override
                                public String getName() {
                                    return null;
                                }

                                @Override
                                public boolean isVoidType() {
                                    return false;
                                }

                                @Override
                                public boolean isValueType() {
                                    return false;
                                }

                                @Override
                                public boolean isSimpleType() {
                                    return false;
                                }

                                @Override
                                public boolean isEnumType() {
                                    return false;
                                }

                                @Override
                                public boolean isStructType() {
                                    return false;
                                }

                                @Override
                                public boolean isNullableType() {
                                    return false;
                                }

                                @Override
                                public boolean isReferenceType() {
                                    return false;
                                }

                                @Override
                                public boolean isClassType() {
                                    return false;
                                }

                                @Override
                                public boolean isInterfaceType() {
                                    return false;
                                }

                                @Override
                                public boolean isNestedType() {
                                    return false;
                                }

                                @Override
                                public ITypeName getDeclaringType() {
                                    return null;
                                }

                                @Override
                                public boolean isDelegateType() {
                                    return false;
                                }

                                @Override
                                public IDelegateTypeName asDelegateTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isArray() {
                                    return false;
                                }

                                @Override
                                public IArrayTypeName asArrayTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isTypeParameter() {
                                    return false;
                                }

                                @Override
                                public ITypeParameterName asTypeParameterName() {
                                    return null;
                                }

                                @Override
                                public boolean isPredefined() {
                                    return false;
                                }

                                @Override
                                public IPredefinedTypeName asPredefinedTypeName() {
                                    return null;
                                }
                            };
                        }

                        @Override
                        public ITypeName getValueType() {
                            return new ITypeName() {
                                @Override
                                public IAssemblyName getAssembly() {
                                    return null;
                                }

                                @Override
                                public INamespaceName getNamespace() {
                                    return null;
                                }

                                @Override
                                public String getFullName() {
                                    return null;
                                }

                                @Override
                                public String getName() {
                                    return valueTypeName;
                                }

                                @Override
                                public boolean isVoidType() {
                                    return false;
                                }

                                @Override
                                public boolean isValueType() {
                                    return false;
                                }

                                @Override
                                public boolean isSimpleType() {
                                    return false;
                                }

                                @Override
                                public boolean isEnumType() {
                                    return false;
                                }

                                @Override
                                public boolean isStructType() {
                                    return false;
                                }

                                @Override
                                public boolean isNullableType() {
                                    return false;
                                }

                                @Override
                                public boolean isReferenceType() {
                                    return false;
                                }

                                @Override
                                public boolean isClassType() {
                                    return false;
                                }

                                @Override
                                public boolean isInterfaceType() {
                                    return false;
                                }

                                @Override
                                public boolean isNestedType() {
                                    return false;
                                }

                                @Override
                                public ITypeName getDeclaringType() {
                                    return null;
                                }

                                @Override
                                public boolean isDelegateType() {
                                    return false;
                                }

                                @Override
                                public IDelegateTypeName asDelegateTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isArray() {
                                    return false;
                                }

                                @Override
                                public IArrayTypeName asArrayTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean isTypeParameter() {
                                    return false;
                                }

                                @Override
                                public ITypeParameterName asTypeParameterName() {
                                    return null;
                                }

                                @Override
                                public boolean isPredefined() {
                                    return false;
                                }

                                @Override
                                public IPredefinedTypeName asPredefinedTypeName() {
                                    return null;
                                }

                                @Override
                                public boolean hasTypeParameters() {
                                    return false;
                                }

                                @Override
                                public List<ITypeParameterName> getTypeParameters() {
                                    return null;
                                }

                                @Override
                                public String getIdentifier() {
                                    return null;
                                }

                                @Override
                                public boolean isUnknown() {
                                    return false;
                                }

                                @Override
                                public boolean isHashed() {
                                    return false;
                                }

                                @Override
                                public int compareTo(ITypeName o) {
                                    return 0;
                                }
                            };
                        }

                        @Override
                        public boolean isStatic() {
                            return isStatic;
                        }

                        @Override
                        public String getName() {
                            return methodName;
                        }

                        @Override
                        public String getFullName() {
                            return methodFullName;
                        }

                        @Override
                        public String getIdentifier() {
                            return identifier;
                        }

                        @Override
                        public boolean isUnknown() {
                            return isUnknown;
                        }

                        @Override
                        public boolean isHashed() {
                            return isHashed;
                        }
                    };

                    List<String> overallContext = deserializeContext(rs.getString("overallcontext"));
                    IndexDocument doc = new IndexDocument(docID, method, overallContext, projectName);
                    documents.add(doc);
                    rs.next();
                }
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;

    }

    /**
     * Deserialize from SQLite
     *
     * @param docID
     * @return
     */
    private IndexDocument deserializeFromSQLite(String docID) {
        String sqlSelect = "SELECT * FROM " + this.SQL_TABLE_NAME + " WHERE docid=\"" + docID + "\"";
        try (Statement stmt = dbConn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sqlSelect)) {
                boolean hasItems = rs.isBeforeFirst();
                if (hasItems) {
                    String methodCall = rs.getString("method");
                    String type = rs.getString("type");
                    List<String> overallContext = deserializeContext(rs.getString("overallcontext"));
                    IndexDocument doc = new IndexDocument(docID, methodCall, type, overallContext);
                    return doc;
                }
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Deserialize the Context
     *
     * @param context
     * @return result
     */
    private List<String> deserializeContext(String context) {
        List<String> result = new LinkedList<>();
        int position = 0;
        if (!context.isEmpty()) {
            while (position < context.length()) {
                int commaPosition = position + context.substring(position).indexOf(",");
                int wordLength = Integer.valueOf(context.substring(position, commaPosition));
                String s = context.substring(commaPosition + 1, commaPosition + 1 + wordLength);
                result.add(s);
                position = commaPosition + wordLength + 1;
            }
        }
        return result;
    }

    /**
     * Deserialization from File
     *
     * @param docID
     * @return doc
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
        } finally {
            in.close();
            fileIn.close();
        }
        return doc;
    }

    /**
     * Method to finish Indexing and close the db connection properly
     */
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

    /**
     * Helper method to split the context
     *
     * @param context
     * @return string
     */
    private String splitContext(List<String> context) {
        StringBuilder sb = new StringBuilder();
        for (String s : context) {
            sb.append(s.length());
            sb.append(",");
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Helper function to create a bool out of an integer
     *
     * @param i
     * @return
     */
    private boolean intToBool(int i) {
        return i == 1;
    }

    /**
     * Helper function to create an int out of a boolean representation
     *
     * @param b
     * @return
     */
    private int booleanToInt(boolean b) {
        if (b == true)
            return 1;
        return 0;
    }
}

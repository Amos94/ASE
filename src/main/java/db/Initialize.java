package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Initialize {

    //The DB connection
    private DBConnection dbConnection;

    //The connection details needed by the db
    private ConnectionDetails cd;

    //name constants needed for the db
    private String SCHEMA_NAME = "ase";
    private String TABLE_NAME = "index";

    //constructor that initializes and creates the schema and the table for the db
    public Initialize(){
        createSchema(SCHEMA_NAME);
        createIndexTable(TABLE_NAME);
    }
  //constructor that initializes and the creates a schema and a table for the db
    public Initialize(String schemaName, String tableName) {
        createSchema(schemaName);
        createIndexTable(tableName);
    }

	/*
	 * A method to create a schema in a db with a given name.
	 * @param schemaName	A string with the name of the schema to create.
	 */
    public void createSchema(String schemaName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());

       try {
            //create the statement and execute it
        	Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.createSchema(schemaName));
            //close the db connection
            dbConnection.closeConnection();
       	} catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * A method to create a table in a db.
     * @param tableName		A string with the name of the table to create.
     */
    public void createIndexTable(String tableName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());

        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.createIndexTable(tableName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * A method to insert rows into a table in a db.
     * @param insertion		A string with the complete index entry. The String has to have the form: "id,id,id;type;method;parameter,parameter"
     * @param tableName		A string with the name of the table.
     */
    public void insertIntoIndexTable(String insertion, String tableName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
      //TODO adding a file
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.insertIndexTable(insertion, tableName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * A methode to update a table in a db. (! If the value is a string add '' around it, if its a array add () around it)
     * @param update 		A string to update the table. The String has to have the form: "id,id,id;type;method;parameter,parameter;rowToUpdate=Value"
     * @param tableName		A string with the name of the table.
     */
    public void updateIndexTable(String update, String tableName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.updateIndexTable(update, tableName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * A method to delete a row in a db.
     * @param deletion		A string to delete the row. The String has to have the form: "rowToDelete='Value'"
     * @param tableName		A string with the name of the table to delete.
     */
    public void deleteRow(String deletion, String tableName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.deleteRow(deletion, tableName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * A method to delete a table in a db.
     * @param tableName		A string with the name of the table to delete.
     */
    public void deleteIndexTable(String tableName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.deleteIndexTable(tableName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * A method to delete a schema in a db.
     * @param schemaName	A string with the name of the schema to create.
     */
    public void deleteSchema(String schemaName){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.deleteSchema(schemaName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*	A method to get the complete content of a table of a db.
     * @param tableName		A string with the name of the table.
     */
    public String getIndexTable(String tableName){
    	//The result string
    	String result="";
    	
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());

        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            ResultSet test =  st.executeQuery(Query.getIndex(tableName));
            //Iterate over ResultSet
            while(test.next()){
                //Retrieve by column name
            	String lookback = test.getString("Lookback");
                String declaringType  = test.getString("NameOfDeclaringType");
                String methodName = test.getString("MethodName");
                String parameterType = test.getString("ParameterType");
                result = result.concat(lookback+ "-"+declaringType+ "-"+methodName+ "-"+parameterType);
                //each row is stored as a line
                if(test.next()) {
                	result= result.concat("\n");
                }
            }
            
            //close db connection
            dbConnection.closeConnection();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}

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

    //Create schema with predefined query
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

    //Create the index table needed
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
    
    //Insert into the index table
    public void insertIntoIndexTable(String insertion){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
      //TODO adding a file
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.insertIndexTable(insertion, TABLE_NAME));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Update  the index table
    public void updateIndexTable(String update){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.updateIndexTable(update, TABLE_NAME));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Delete a row
    public void deleteRow(String deletion){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.deleteRow(deletion, TABLE_NAME));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Delete a table
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

    //Delete a schema
    public void deleteSchema(String schema){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());
        
        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            st.execute(Query.deleteSchema(schema));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Return table
    public void getIndexTable(){
        //new db connection
        dbConnection = new DBConnection(cd.URL.getConf(),cd.USER.getConf(),cd.PASSWORD.getConf());

        try {
            //create statement and execute it
            Statement st = dbConnection.getConnection().createStatement();
            ResultSet test =  st.executeQuery(Query.getIndex(TABLE_NAME));
            while(test.next()){
                //Retrieve by column name
            	String lookback = test.getString("Lookback");
                String declaringType  = test.getString("NameOfDeclaringType");
                String methodName = test.getString("MethodName");
                String parameterType = test.getString("ParameterType");
                System.out.println( lookback + declaringType + methodName + parameterType);
                
            }
            //st.execute(Query.createIndexTable(tableName));
            //close db connection
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

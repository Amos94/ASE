package db;

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

}

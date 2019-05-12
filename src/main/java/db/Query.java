package db;

public class Query {

    public static String createSchema(String schemaName){

        return "INIT=CREATE SCHEMA IF NOT EXISTS "+schemaName;
    }

    public static String createIndexTable(String indexTable){
        return "CREATE TABLE IF NOT EXISTS "+indexTable+"();";//TODO define the structure;
    }

    public static String insertIndexTable(String insertion){
        return ""; //TODO to be done after we define the structure
    }

    public static String updateIndexTable(String update){
        return null; //TODO to be done after we define the structure
    }

    public static String getIndex(){
        return null; //TODO to be done after we define the structure
    }

}

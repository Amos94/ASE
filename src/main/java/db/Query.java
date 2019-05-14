package db;

public class Query {

	/*
	 * A method to create a schema in a db with a given name.
	 * @param schemaName	A string with the name of the schema to create.
	 */
    public static String createSchema(String schemaName){

        return "CREATE SCHEMA IF NOT EXISTS "+schemaName;
    }
    
    /*
     * A method to create a table in a db.
     * @param indexTable	A string with the name of the table to create.
     */
    public static String createIndexTable(String indexTable){
        return "CREATE TABLE IF NOT EXISTS "+indexTable+"("+
        		"Lookback ARRAY, " + 
        		"NameOfDeclaringType varchar(255) NOT NULL, " + 
        		"MethodName varchar(255) NOT NULL, " +  
        		"ParameterType ARRAY NOT NULL);" ; 
    }

    /*
     * A method to insert rows into a table in a db.
     * @param insertion		A string with the complete index entry. The String has to have the form: "id,id,id;type;method;parameter,parameter"
     * @param indexTable	A string with the name of the table.
     * @return				Returns the String to use for the db.
     * 
     */
    
    public static String insertIndexTable(String insertion, String indexTable){
    	String[] parts = insertion.split(";");
    	
    	String part = parts[0]; 
    	StringBuilder str = new StringBuilder();
    	String[] look = part.split(",");
    	for (int i = 0; i < look.length; i++) {
    		str = str.append("'" + look[i] + "',");    
    		}
    	String lookback = str.toString();
    	
    	String nameOfDeclaringType = parts[1]; 
    	String methodName = parts[2];
    	
    	StringBuilder builder = new StringBuilder();
    	String types = parts[3];
    	String[] parameter = types.split(",");
    	for (int i = 0; i < parameter.length; i++) {
    		builder = builder.append("'" + parameter[i] + "',");    		  
    		}
    	String parameterType = builder.toString();    	
    	
        return "INSERT INTO " + indexTable + " VALUES "+
        		"((" + lookback + "),'" + nameOfDeclaringType + "','" + methodName + "',(" + parameterType + "));" ;
    }
    
    /*
     * A methode to update a table in a db.
     * @param update 		A string to update the table. The String has to have the form: "id,id,id;type;method;parameter,parameter;rowToUpdate=Value"
     * @param indexTable	A string with the name of the table.
     * @return 				returns the String to use for the db
     */

    public static String updateIndexTable(String update, String indexTable){
    	String[] parts = update.split(";");
    	
    	String part = parts[0]; 
    	StringBuilder str = new StringBuilder();
    	String[] look = part.split(",");
    	for (int i = 0; i < look.length; i++) {
    		str = str.append("'" + look[i] + "',");    		  
    		}
    	String lookback = str.toString();
    	
    	String nameOfDeclaringType = parts[1]; 
    	String methodName = parts[2];
    	
    	StringBuilder builder = new StringBuilder();
    	String types = parts[3];
    	String[] parameter = types.split(",");
    	for (int i = 0; i < parameter.length; i++) {
    		builder = builder.append("'" + parameter[i] + "',");    		  
    		}
    	String parameterType = builder.toString();    	
    	
    	String updateString = parts[4];
    	
         return "UPDATE " + indexTable +  " SET " + "Lookback=(" + lookback + "), NameOfDeclaringType='" +nameOfDeclaringType+ "', MethodName='"+ methodName +
        		 "', ParameterType=("+ parameterType +") WHERE " +updateString +";"; 
    }
    
    /*
     * A method to delete a row in a db.
     * @param deletion		A string to delete the row. The String has to have the form: "rowToDelete='Value'"
     * @param indexTable	A string with the name of the table to delete.
     */
    public static String deleteRow(String deletion, String indexTable) {
    	return "DELETE FROM " + indexTable + " WHERE " + deletion; 
    } //TODO some problems with deleting array that are field with char   
    
    /*
     * A method to delete a table in a db.
     * @param indexTable	A string with the name of the table to delete.
     */
    public static String deleteIndexTable(String indexTable) {
    	return "DROP TABLE IF EXISTS " + indexTable; 
    }
    
    /*
     * A method to delete a schema in a db.
     * @param schemaName	A string with the name of the schema to create.
     */
    public static String deleteSchema(String schemaName) {
    	return "DROP SCHEMA IF EXISTS " + schemaName; 
    }
    
    /*	A method to get the complete content of a table of a db.
     * @param indexTable	A string with the name of the table.
     */
    public static String getIndex(String indexTable){
    	
        return "SELECT * FROM " +  indexTable + ";";
    }
    
}

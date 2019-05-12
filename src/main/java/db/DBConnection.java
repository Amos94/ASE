package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private Connection conn;
    public DBConnection(String url, String user, String password){

        try {
            this.conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void closeConnection(){

        try {
            if(!this.conn.isClosed()){
                try {
                    this.conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.conn;
    }


}

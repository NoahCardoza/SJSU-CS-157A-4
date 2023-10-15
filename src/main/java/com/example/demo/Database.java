package com.example.demo;
import java.sql.*;
public class Database {
    static public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + System.getProperty("DBMS_HOST", "localhost") +":" + System.getProperty("DBMS_PORT", "3306") + "/" + System.getProperty("DBMS_SCHEMA");

            return DriverManager.getConnection(url, System.getProperty("DBMS_USERNAME"), System.getProperty("DBMS_PASSWORD"));
        }catch(Exception e){
            return null;
        }
    }
}

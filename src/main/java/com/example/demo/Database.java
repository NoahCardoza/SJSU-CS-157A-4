package com.example.demo;

import java.sql.*;
public class Database {
    static private Database database;
    Connection connection;
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    private Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            String url = "jdbc:mysql://" + System.getProperty("DBMS_HOST", "localhost") +":" + System.getProperty("DBMS_PORT", "3306") + "/" + System.getProperty("DBMS_SCHEMA");
            connection =  DriverManager.getConnection(url, System.getProperty("DBMS_USERNAME"), System.getProperty("DBMS_PASSWORD"));
        }

        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public Long getLastInsertedId(String table) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT last_insert_id() FROM " + table + ";");
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getLong(1);
    }
}

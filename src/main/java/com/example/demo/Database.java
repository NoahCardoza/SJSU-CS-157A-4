package com.example.demo;

import com.example.demo.beans.entities.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class Database {
    static Connection connection = null;
    static public Connection getConnection() throws SQLException {
        try {
            Context ctx = new InitialContext();
            ctx = (Context) ctx.lookup("java:comp/env");
            DataSource datasource = (DataSource) ctx.lookup("jdbc/hidden_gems");
            if (connection == null) {
                connection = datasource.getConnection();
            } else if (connection.isClosed()) {
                connection = datasource.getConnection();
            }
            return connection;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    static public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    static public Long getLastInsertedId(String table) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("SELECT last_insert_id() FROM " + table + ";");
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getLong(1);
    }
}

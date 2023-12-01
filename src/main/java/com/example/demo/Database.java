package com.example.demo;

import com.example.demo.beans.entities.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class Database {
    static public Connection getConnection() throws SQLException {
        try {
            Context ctx = new InitialContext();
            ctx = (Context) ctx.lookup("java:comp/env");
            DataSource datasource = (DataSource) ctx.lookup("jdbc/hidden_gems");

            return datasource.getConnection();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}

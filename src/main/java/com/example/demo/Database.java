package com.example.demo;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Singleton class for connecting to the database.
 *
 * TODO: remove singleton pattern
 */
public class Database {
    static private Database database;
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    private Database() {}

    public Connection getConnection() throws SQLException {
        try {
            Context ctx = new InitialContext();
            ctx = (Context) ctx.lookup("java:comp/env");
            DataSource datasource = (DataSource) ctx.lookup("jdbc/hidden_gems");
            return datasource.getConnection();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getLastInsertedId(String table) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("SELECT last_insert_id() FROM " + table + ";");
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getLong(1);
    }
}

package com.example.demo.servlets;

import com.example.demo.Database;
import jakarta.servlet.http.HttpServlet;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHttpServlet extends HttpServlet {

    @Override
    public void destroy() {
        super.destroy();
        try {
            Database.getInstance().closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

package com.example.demo;

import java.io.*;
import java.sql.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/cardoza";

        String username = "root";
        String password = "Fx.u2K_A4wq2MXoABzAU";

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        StringBuilder output = new StringBuilder("Users:<br/>");

        Connection conn = getConnection();
        if (conn != null) {
            try {
            Statement stmt = null;
                stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Student");
            while (rs.next())
                output.append(rs.getInt(1)).append(" ").append(rs.getString(2)).append(" ").append(rs.getString(3)).append("<br/>");
            conn.close();

        } catch(SQLException e){
            System.out.println(e);
        }
        }
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<div>" + output.toString() + "</div>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
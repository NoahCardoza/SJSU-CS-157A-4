package com.example.demo;

import java.sql.*;

// Notice, do not import com.mysql.cj.jdbc.*
// or you will have problems!

public class LoadDriver {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/cardoza";

        String username = "root";
        String password = "Fx.u2K_A4wq2MXoABzAU";


        try {
            conn = DriverManager.getConnection(url, username, password);

            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("select * from Student");
            while(rs.next())
                System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3));
            conn.close();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }
}

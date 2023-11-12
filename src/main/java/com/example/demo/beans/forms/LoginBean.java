package com.example.demo.beans.forms;

import com.example.demo.Database;
import com.example.demo.Security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginBean {
    private String email;
    private String password;

    public LoginBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long validate() throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT id, password FROM User WHERE email=?");

        ps.setString(1, getEmail());

        ResultSet rs = ps.executeQuery();

        if (rs.next()){
            if (Security.checkPassword(getPassword(), rs.getString("password"))) {
                return rs.getLong(1);
            }
        }

        return null;
    }
}

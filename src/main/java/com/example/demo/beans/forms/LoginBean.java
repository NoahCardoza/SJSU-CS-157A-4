package com.example.demo.beans.forms;

import com.example.demo.Database;

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
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT id FROM User WHERE email=? AND password=?");

        ps.setString(1, getEmail());
        ps.setString(2, getPassword());

        ResultSet rs = ps.executeQuery();

        if (rs.next()){
            return rs.getLong(1);
        }

        return null;
    }
}

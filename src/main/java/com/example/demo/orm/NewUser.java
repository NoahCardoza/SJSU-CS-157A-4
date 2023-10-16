package com.example.demo.orm;

import com.example.demo.Database;

import java.sql.*;

public class NewUser {

    Integer userId = 0;
    String username = "";
    String password = "";
    String email = "";

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer integer) {
        this.userId = integer;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void createNewUser(){
        try {
            Connection conn = Database.getConnection();

            if (conn == null) {
                return;
            }

            PreparedStatement statement = conn.prepareStatement("INSERT INTO User (id, username, email, password) VALUES (?, ?, ?, ?)");
            statement.setInt(1, this.getUserId());
            statement.setString(2, this.getUsername());
            statement.setString(3, this.getEmail());
            statement.setString(4, this.getPassword());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

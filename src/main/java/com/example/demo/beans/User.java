package com.example.demo.beans;

import com.example.demo.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

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
}

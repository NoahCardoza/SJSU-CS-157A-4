package com.example.demo.daos;

import com.example.demo.Database;
import java.sql.*;

public class UserDao {

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

            PreparedStatement statement = conn.prepareStatement("INSERT INTO User (username, email, normalized_email, password) VALUES (?, ?, ?, ?)");
            statement.setString(1, this.getUsername());
            statement.setString(2, this.getEmail());
            statement.setString(3, this.getEmail());
            statement.setString(4, this.getPassword());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int isUnique(){
        Connection conn = Database.getConnection();

        if (conn == null) {
            return 0;
        }

        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * FROM User WHERE username=? OR email=?");

            ps.setString(1, getUsername());
            ps.setString(2, getEmail());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()){
                return 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}

package com.example.demo.bean;

import com.example.demo.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Alert {
    public String color;
    public String message;

    public  Alert() {};

    public Alert(String color, String message) {
        this.color = color;
        this.message = message;
    }

    public String getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

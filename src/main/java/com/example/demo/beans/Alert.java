package com.example.demo.beans;

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

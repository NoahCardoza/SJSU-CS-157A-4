package com.example.demo.beans;

public class User {

    Long id;
    String username;
    String password;
    String email;

    public Long getId() {
        return id;
    }

    public void setId(Long integer) {
        this.id = integer;
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

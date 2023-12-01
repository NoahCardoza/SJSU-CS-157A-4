package com.example.demo.beans.forms;

import com.example.demo.Validation;
import jakarta.servlet.http.HttpServletRequest;

public class SignupForm {

    String name = "";
    private String email = "";
    private String password = "";
    private String username = "";

    public SignupForm() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public SignupForm(HttpServletRequest request) {
        this.username = request.getParameter("username");
        this.password = request.getParameter("password");
        this.email = request.getParameter("email");
        this.name = request.getParameter("name");
    }

    public Validation validate(){
        Validation v = new Validation();

        if(name == null){
            v.addMessage("Name is required.");
        }

        if(username == null){
            v.addMessage("Username is required.");
        }

        if(password == null){
            v.addMessage("Password is required.");
        }

        if(email == null){
            v.addMessage("Email is required");
        }

        if (!v.isValid()) {
            return v;
        }

        name = name == null ? "" : name.trim();
        if (this.name.isEmpty()) {
            v.addMessage("Name is required.");
        }

        username = username == null ? "" : username.trim();
        if (this.username.isEmpty()) {
            v.addMessage("Username is required.");
        }

        password = password == null ? "" : password.trim();
        if (this.password.isEmpty()) {
            v.addMessage("Password is required.");
        }

        email = email == null ? "" : email.trim();
        if (this.email.isEmpty()) {
            v.addMessage("Email is required.");
        }

        return v;

    }
}

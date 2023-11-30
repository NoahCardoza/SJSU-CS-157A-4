package com.example.demo.beans.forms;

import jakarta.servlet.http.HttpServletRequest;

public class UserForm {
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;

    public UserForm(String username, String email, String oldPassword, String newConfirmation) {
        this.username = username;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newConfirmation;
    }

    public UserForm(HttpServletRequest request) {
        this.username = request.getParameter("username");
        this.email = request.getParameter("email");
        this.oldPassword = request.getParameter("oldPassword");
        this.newPassword = request.getParameter("newPassword");
    }

    public String validate() {
        if (username == null || username.isBlank()) {
            return "Username is required.";
        }

        if (email == null || email.isBlank()) {
            return "Email is required.";
        }

        if (!(newPassword == null || newPassword.isBlank())) {
            if (oldPassword == null || oldPassword.isBlank()) {
                return "Old password is required.";
            }

            if (newPassword.equals(oldPassword)) {
                return "Password and new password cannot be the same.";
            }
        }

        return null;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

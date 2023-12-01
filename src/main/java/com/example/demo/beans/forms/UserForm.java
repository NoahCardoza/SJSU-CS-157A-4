package com.example.demo.beans.forms;

import jakarta.servlet.http.HttpServletRequest;

public class UserForm {
    private String username;
    private String email;
    private String name;
    private String oldPassword;
    private String newPassword;
    private Boolean isPrivateProfile;

    private boolean passwordChanged = false;

    public UserForm(HttpServletRequest request) {
        this.username = request.getParameter("username");
        this.email = request.getParameter("email");
        this.name = request.getParameter("name");
        this.oldPassword = request.getParameter("oldPassword");
        this.newPassword = request.getParameter("newPassword");
        this.isPrivateProfile = request.getParameter("isPrivate") != null;
    }

    public String validate() {
        if (username == null || username.isBlank()) {
            return "Username is required.";
        }

        if (email == null || email.isBlank()) {
            return "Email is required.";
        }

        if (name == null || name.isBlank()) {
            return "Name is required.";
        }

        if (!(newPassword == null || newPassword.isBlank())) {
            if (oldPassword == null || oldPassword.isBlank()) {
                return "Old password is required.";
            }

            if (newPassword.equals(oldPassword)) {
                return "Password and new password cannot be the same.";
            }

            passwordChanged = true;
        } else if (!(oldPassword == null || oldPassword.isBlank())) {
            return "New password was not entered.";
        }

        return null;
    }

    public Boolean getPrivateProfile() {
        return isPrivateProfile;
    }

    public void setPrivateProfile(Boolean aPrivate) {
        isPrivateProfile = aPrivate;
    }

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

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}

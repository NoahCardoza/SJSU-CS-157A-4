package com.example.demo;

import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Guard {
    static public User requireAuthenticationWithMessage(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        return requireAuthenticationWithMessage(request, response, message, true);
    }

    static public void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.sendRedirect(request.getContextPath() + "/login");
    }

    static public void redirectToLogin(HttpServletRequest request, HttpServletResponse response, Alert alert) throws IOException {
        response.setStatus(401);
        request.getSession().setAttribute("alert", alert);

        String redirect = request.getRequestURI();
        if (request.getQueryString() != null) {
            redirect += "?" + request.getQueryString();
        }
        redirect = URLEncoder.encode(redirect, StandardCharsets.UTF_8);

        response.sendRedirect(request.getContextPath() + "/login?redirect=" + redirect);
    }

    static public User requireAuthenticationWithMessage(HttpServletRequest request, HttpServletResponse response, String message, Boolean withRedirect) throws IOException {
        User user = (User) request.getAttribute("user");

        if (user == null) {
            if (withRedirect) {
                redirectToLogin(request, response, new Alert("danger", message));
            } else {
                redirectToLogin(request, response);
            }
        }
        return user;
    }

    public static void redirectToHome(HttpServletRequest request, HttpServletResponse response, int status, Alert alert) throws IOException {
        redirectTo(request, response, status, "/", alert);
    }

    public static Long getLongParameter(HttpServletRequest request, HttpServletResponse response, String key) throws IOException {
        Long value = Util.parseLongOrNull(request.getParameter(key));
        if (value == null) {
            redirectToHome(request, response, 400, new Alert("danger", "Invalid " + key + " parameter"));
        }
        return value;
    }

    public static void redirectTo(HttpServletRequest request, HttpServletResponse response, int status, String path, Alert alert) throws IOException {
        response.setStatus(status);
        if (alert != null) {
            request.getSession().setAttribute("alert", alert);
        }
        response.sendRedirect(request.getContextPath() + path);
    }
}

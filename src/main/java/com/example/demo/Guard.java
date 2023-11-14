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
    static public User requireAuthenticationWithMessage(HttpServletRequest request, HttpServletResponse response, String message, Boolean withRedirect) throws IOException {
        User user = (User) request.getAttribute("user");

        if (user == null) {
            response.setStatus(401);
            request.getSession().setAttribute(
                    "alert",
                    new Alert("danger", message)
            );

            if (withRedirect) {
                String redirect = request.getRequestURI();
                if (request.getQueryString() != null) {
                    redirect += "?" + request.getQueryString();
                }
                redirect = URLEncoder.encode(redirect, StandardCharsets.UTF_8);

                response.sendRedirect(request.getContextPath() + "/login?redirect=" + redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        }
        return user;
    }
}

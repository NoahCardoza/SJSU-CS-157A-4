package com.example.demo.servlets.auth;

import com.example.demo.beans.Alert;
import com.example.demo.beans.forms.LoginBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Login", value = "/login")
public class LoginServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LoginBean bean = new LoginBean(request.getParameter("email"), request.getParameter("password"));

        Long userId = null;
        try {
            userId = bean.validate();

            if (userId != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                response.sendRedirect("index.jsp");
            } else {
                request.setAttribute(
                    "alert",
                    new Alert("danger", "That email/password combination cannot be found in our records")
                );

                request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute(
                "alert",
                new Alert("danger", "An error occurred while logging in. Please try again later.")
            );
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
    }
}
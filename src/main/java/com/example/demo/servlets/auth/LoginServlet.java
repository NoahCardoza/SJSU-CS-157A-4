package com.example.demo.servlets.auth;

import com.example.demo.Database;
import com.example.demo.Security;
import com.example.demo.beans.Alert;
import com.example.demo.beans.forms.LoginBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "Login", value = "/login")
public class LoginServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LoginBean bean = new LoginBean(request.getParameter("email"), request.getParameter("password"));
        Long userId;
        try {
            try (Connection conn = Database.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT id, password, verified FROM User WHERE email=?");
                ps.setString(1, bean.getEmail());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    if (!rs.getBoolean("verified")) {
                        request.setAttribute(
                                "alert",
                                new Alert("danger", "You must verify your email before logging in.")
                        );
                        request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
                        return;
                    }
                    if (!Security.checkPassword(bean.getPassword(), rs.getString("password"))) {
                        request.setAttribute(
                                "alert",
                                new Alert("danger", "That email/password combination cannot be found in our records")
                        );
                        request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
                        return;
                    }
                    userId = rs.getLong("id");
                } else {
                    request.setAttribute(
                            "alert",
                            new Alert("danger", "That email/password combination cannot be found in our records")
                    );
                    request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
                    return;
                }
            }

            HttpSession session = request.getSession();
            session.setAttribute("user_id", userId);
            if (request.getParameter("remember_me") != null) {
                session.setMaxInactiveInterval(60 * 60 * 24 * 30);
            }
            String redirect = request.getParameter("redirect");

            if (redirect != null) {
                response.sendRedirect(request.getContextPath() + redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
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
        String f = request.getParameter("f");

        if (f != null && f.equals("logout")) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
    }
}
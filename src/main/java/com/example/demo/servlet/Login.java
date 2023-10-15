package com.example.demo.servlet;

import com.example.demo.Database;
import com.example.demo.bean.Alert;
import com.example.demo.bean.LoginBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {
    private String message;

    public void init() {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LoginBean bean = new LoginBean(request.getParameter("email"), request.getParameter("password"));

        int userId = bean.validate();

        System.out.println(userId);

        if (userId != 0) {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", Integer.toString(userId));
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute(
                "alert",
                new Alert("danger", "That email/password combination cannot be found in our records")
            );

            request.getRequestDispatcher("template/login.jsp").forward(request, response);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("template/login.jsp").forward(request, response);
    }

    public void destroy() {
    }
}
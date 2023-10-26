package com.example.demo.servlet;

import com.example.demo.beans.Alert;
import com.example.demo.beans.forms.LoginBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {
    private String message;

    public void init() {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LoginBean bean = new LoginBean(request.getParameter("email"), request.getParameter("password"));

        int userId = bean.validate();

        if (userId != 0) {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", Integer.toString(userId));
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute(
                "alert",
                new Alert("danger", "That email/password combination cannot be found in our records")
            );

            request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("template/auth/login.jsp").forward(request, response);
    }

    public void destroy() {
    }
}
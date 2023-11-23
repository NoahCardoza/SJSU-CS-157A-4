package com.example.demo.servlets.auth;

import com.example.demo.*;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.User;
import com.example.demo.beans.forms.SignupForm;
import com.example.demo.daos.UserDao;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Signup", value = "/signup")
public class SignupServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String f = request.getParameter("f");
        if (f != null && f.equals("verify")) {
            String token = request.getParameter("token");
            if (token == null) {
                response.getWriter().println("Invalid token");
                response.setStatus(400);
                return;
            }

            Long userId = JWTManager.getInstance().verifyEmailVerificationToken(token);

            if (userId == -1) {
                response.getWriter().println("Invalid token");
                response.setStatus(400);
                return;
            }

            try {
                UserDao.getInstance().verifyEmail(userId);
                Guard.redirectToLogin(request, response, new Alert("success", "Your email has been verified!"));
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("Invalid token");
                response.setStatus(400);
                return;
            }
        }

        request.getRequestDispatcher("/template/auth/signup.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SignupForm form = new SignupForm(request);

        String action = request.getParameter("action");

        if (action != null && action.equals("submit")) {
            Validation v = form.validate();

            if (v.isValid()) {
                User user = new User();

                user.setUsername(form.getUsername());
                user.setPassword(Security.hashPassword(form.getPassword()));
                user.setEmail(form.getEmail());
                String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

                // this disables email verification for localhost
                if (baseUrl.contains("localhost")) {
                    user.setVerified(true);
                } else {
                    user.setVerified(false);
                }

                try {
                    if (UserDao.getInstance().isUnique(user)) {
                        UserDao.getInstance().create(user);

                        if (user.isVerified()) {
                            Guard.redirectToLogin(
                                    request,
                                    response,
                                    new Alert("success", "Your account has been created!")
                            );
                            return;
                        } else {
                            try {
                                Emailer.sendVerificationEmail(user, baseUrl);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                UserDao.getInstance().delete(user);
                                request.getSession().setAttribute(
                                        "alert",
                                        new Alert("danger", "An error occurred while sending the verification email. Please try again later.")
                                );

                            }
                            Guard.redirectToLogin(
                                    request,
                                    response,
                                    new Alert("success", "Your account has been created! Please check your email to verify your account.")
                            );
                            return;
                        }
                    } else {
                        request.setAttribute(
                                "alert",
                                new Alert("danger", "This username or email is already in use.")
                        );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute(
                            "alert",
                            new Alert("danger", "An error occurred while creating your account. Please try again later.")
                    );
                }
            }
            else {
                request.setAttribute("errors", v.getMessages());
            }
        }

        request.setAttribute("form", form);

        request.getRequestDispatcher("/template/auth/signup.jsp").forward(request, response);
    }
}

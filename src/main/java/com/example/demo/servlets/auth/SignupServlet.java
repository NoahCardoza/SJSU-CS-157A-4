package com.example.demo.servlets.auth;

import com.example.demo.Validation;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.User;
import com.example.demo.beans.forms.SignupForm;
import com.example.demo.daos.UserDao;

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
                user.setPassword(form.getPassword());
                user.setEmail(form.getEmail());

                try {
                    if (UserDao.getInstance().isUnique(user)) {
                        UserDao.getInstance().create(user);

                        request.getSession().setAttribute("user_id", user.getId());

                        response.sendRedirect("index.jsp");
                        return;
                    } else {
                        request.setAttribute(
                                "alert",
                                new Alert("danger", "Duplicate username/password")
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
package com.example.demo.servlet;

import com.example.demo.Validation;
import com.example.demo.bean.Alert;
import com.example.demo.bean.SignupForm;
import com.example.demo.orm.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Signup", value = "/signup")
public class Signup extends HttpServlet {
    public void init() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/template/signup.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SignupForm form = new SignupForm(request);

        String action = request.getParameter("action");

        if (action != null && action.equals("submit")) {
            Validation v = form.validate();

            if (v.isValid()) {
                User user = new User();
                // eventually user session id for user.setUserId(7);
                user.setUsername(form.getUsername());
                user.setPassword(form.getPassword());
                user.setEmail(form.getEmail());

                if(user.isUnique() != 0){
                    user.createNewUser();

                    response.sendRedirect("index.jsp");
                    return;
                }
                else {
                    request.setAttribute(
                            "alert",
                            new Alert("danger", "Duplicate username/password")
                    );
                }
            }
            else {
                request.setAttribute("errors", v.getMessages());
            }
        }

        request.setAttribute("form", form);

        request.getRequestDispatcher("/template/signup.jsp").forward(request, response);
    }


    public void destroy() {
    }
}

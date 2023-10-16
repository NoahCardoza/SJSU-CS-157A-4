package com.example.demo.servlet;

import com.example.demo.Validation;
import com.example.demo.bean.LocationForm;
import com.example.demo.bean.SignupBean;
import com.example.demo.bean.LoginBean;
import com.example.demo.orm.Location;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Signup", value = "/signup")
public class Signup extends HttpServlet {
    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/template/signup.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        try {
        SignupBean.SignupForm form = new SignupForm(request);

        String action = request.getParameter("action");

        if (action != null && action.equals("submit")) {
            Validation v = form.validate();
            if (v.isValid()) {
                Location location = new Location();
                location.setName(form.getName());
                location.setDescription(form.getDescription());
                location.setAddress(form.getAddress());
                location.setLatitude(form.getLatitude());
                location.setLongitude(form.getLongitude());
                location.setParentLocationId(form.getParentId());

                location.create();

                response.sendRedirect("templates/login.jsp");
                return;
            } else {
                request.setAttribute("errors", v.getMessages());
            }
        }

        request.setAttribute("form", form);

        request.getRequestDispatcher("/template/signup.jsp").forward(request, response);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }


    public void destroy() {
    }
}

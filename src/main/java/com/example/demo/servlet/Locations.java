package com.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Locations", value = "/locations")
public class Locations extends HttpServlet {
    private String message;

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<com.example.demo.orm.Location> locations = com.example.demo.orm.Location.getAllLocations();

            for (int i = 0; i < locations.size(); i++) {
                System.out.println(locations.get(i).getId());
            }

            request.setAttribute(
                    "locations",
                    locations
            );

            request.getRequestDispatcher("template/locations.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void destroy() {
    }
}
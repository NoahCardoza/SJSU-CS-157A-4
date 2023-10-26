package com.example.demo.servlets.amenities;

import com.example.demo.beans.Location;
import com.example.demo.daos.LocationDao;
import com.example.demo.servlets.DatabaseHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Amenities", value = "/amenities")
public class Amenities extends DatabaseHttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String rawLocationId = request.getParameter("locationId");

        if (rawLocationId == null) {
            return;
        }

        Long locationId = Long.parseLong(request.getParameter("locationId"));

        // return template listing all amenities
    }
}
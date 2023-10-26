package com.example.demo.servlets.search;

import com.example.demo.Validation;
import com.example.demo.beans.*;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.*;
import com.example.demo.servlets.DatabaseHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "Search", value = "/search")
public class Search extends DatabaseHttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            search(request, response);
        } catch (SQLException | ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void search(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

        request.setAttribute(
                "amenityTypes",
                amenityTypes
        );

        Long amenityTypeId = request.getParameter("amenityTypeId") == null ? null : Long.parseLong(request.getParameter("amenityTypeId"));

        if (amenityTypeId != null) {

            List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(amenityTypeId);

            request.setAttribute(
                    "amenityTypeAttributes",
                    amenityTypeAttributes
            );
        }

        List<Amenity> amenities = AmenityDao.getInstance().getWithFilter(amenityTypeId);

        request.setAttribute(
                "amenities",
                amenities
        );

        request.getRequestDispatcher("template/search/index.jsp").forward(request, response);
    }
}
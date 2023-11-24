package com.example.demo.servlets.search;

import com.example.demo.beans.entities.AmenityType;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.beans.entities.AmenityWithImage;
import com.example.demo.beans.entities.Location;
import com.example.demo.daos.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet(name = "Search", value = "/search")
public class SearchServlet extends HttpServlet {
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

    static public void setSearchAttributes(HttpServletRequest request, List<Location> locations) throws SQLException {

        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

        request.setAttribute(
                "amenityTypes",
                amenityTypes
        );

        AmenityFilter amenityFilter = new AmenityFilter(request);

        if (amenityFilter.getAmenityTypeId() != null) {
            List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(amenityFilter.getAmenityTypeId());

            var amenityTypeAttributeGrouper = new AmenityTypeAttributeGrouper(request, amenityTypeAttributes);

            request.setAttribute(
                    "amenityTypeAttributes",
                    amenityTypeAttributeGrouper
            );
        }

        List<AmenityWithImage> amenities = AmenityDao.getInstance().getWithFilter(amenityFilter, locations);

        request.setAttribute(
                "amenities",
                amenities
        );
    }

    public void search(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        setSearchAttributes(request, null);
        request.getRequestDispatcher("template/search/index.jsp").forward(request, response);
    }
}
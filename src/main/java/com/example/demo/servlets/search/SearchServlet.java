package com.example.demo.servlets.search;

import com.example.demo.beans.entities.AmenityType;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.beans.entities.AmenityWithImage;
import com.example.demo.daos.*;
import com.example.demo.servlets.DatabaseHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Search", value = "/search")
public class SearchServlet extends DatabaseHttpServlet {
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

        AmenityFilter amenityFilter = new AmenityFilter(request);

        if (amenityFilter.getAmenityTypeId() != null) {
            List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(amenityFilter.getAmenityTypeId());

            var amenityTypeAttributeGrouper = new AmenityTypeAttributeGrouper(request, amenityTypeAttributes);

            request.setAttribute(
                    "amenityTypeAttributes",
                    amenityTypeAttributeGrouper
            );
        }


        List<AmenityWithImage> amenities = AmenityDao.getInstance().getWithFilter(amenityFilter);

        request.setAttribute(
                "amenities",
                amenities
        );

        request.getRequestDispatcher("template/search/index.jsp").forward(request, response);
    }
}
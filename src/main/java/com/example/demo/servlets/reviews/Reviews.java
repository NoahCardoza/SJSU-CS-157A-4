package com.example.demo.servlets.reviews;

import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.AmenityWithImage;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.User;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.AmenityDao;
import com.example.demo.daos.LocationDao;
import com.example.demo.daos.UserDao;
import com.example.demo.servlets.DatabaseHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "Reviews", value = "/reviews")
public class Reviews extends DatabaseHttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doRequest(HttpServletRequest request, HttpServletResponse response) {
        String function = request.getParameter("f");

        if (function == null) {
            function = "index";
        }

        try {
            switch (function) {
                case "get":
                    get(request, response);
                case "create":
                    create(request, response);
                    break;
                case "edit":
                    request.getRequestDispatcher("/template/reviews/edit-review.jsp").forward(request, response);
                    break;
                case "delete":
                    request.getRequestDispatcher("/template/reviews/delete-review.jsp").forward(request, response);
                    break;
                default:
                    getAll(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void get(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

//        Long locationId = Util.parseLongOrNull(request.getParameter("id"));
//
//        if (locationId == null) {
//            response.sendRedirect(request.getContextPath() + "/reviews");
//            return;
//        }
//
//        Optional<Location> location = LocationDao.getInstance().get(locationId);
//
//        if (location.isPresent()) {
//            request.setAttribute(
//                    "review",
//                    location.get()
//            );
//        } else {
//            System.out.println("Review not found");
//            response.sendRedirect(request.getContextPath() + "/reviews");
//            return;
//        }
//
//        List<AmenityWithImage> amenities = AmenityDao.getInstance().getOfLocationId(locationId);
//
//        request.setAttribute(
//                "amenities",
//                amenities
//        );

        request.getRequestDispatcher("/template/reviews/get.jsp").forward(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        switch (request.getMethod()) {
            case "GET":
                List<Location> locations = LocationDao.getInstance().getParentLocationsOf(null);

                request.setAttribute("hasParent", false);
                request.setAttribute("locations", locations);

                request.getRequestDispatcher("/template/reviews/form.jsp").forward(request, response);
                break;
            case "POST":
                LocationForm form = new LocationForm(request);

                String action = request.getParameter("action");

                if (action != null && action.equals("submit")) {
                    Optional<User> user = UserDao.getInstance().fromSession(request.getSession());
                    if (!user.isPresent()) {
                        response.setStatus(401);
                        request.setAttribute(
                                "alert",
                                new Alert("danger", "You must be logged in to create a location.")
                        );
                        response.sendRedirect(request.getContextPath() + "/login");
                        return;
                    }
                    Validation v = form.validate();

                    if (v.isValid()) {
                        Location location = new Location();
                        location.setUserId(user.get().getId());
                        location.setName(form.getName());
                        location.setDescription(form.getDescription());
                        location.setAddress(form.getAddress());
                        location.setLatitude(form.getLatitude());
                        location.setLongitude(form.getLongitude());
                        location.setParentLocationId(form.getParentId());
                        try {
                            LocationDao.getInstance().create(location);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        // TODO: redirect to location page

                        response.sendRedirect(request.getContextPath() + "/reviews");
                        return;
                    } else {
                        request.setAttribute("errors", v.getMessages());
                    }
                }

                request.setAttribute("form", form);

                request.getRequestDispatcher("/template/reviews/form.jsp").forward(request, response);
                break;
        }
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Location> locations = LocationDao.getInstance().getAll();

        request.setAttribute(
                "reviews",
                locations
        );

        request.getRequestDispatcher("/template/reviews/index.jsp").forward(request, response);
    }
}
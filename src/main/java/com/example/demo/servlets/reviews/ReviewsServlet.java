package com.example.demo.servlets.reviews;

import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.*;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "Reviews", value = "/reviews")
public class ReviewsServlet extends HttpServlet {
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
//        switch (request.getMethod()) {
//            case "GET":
//                List<Review> reviews = ReviewDao.getInstance().getParentLocationsOf(null);
//
//                request.setAttribute("hasParent", false);
//                request.setAttribute("reviews", reviews);
//
//                request.getRequestDispatcher("/template/reviews/form.jsp").forward(request, response);
//                break;
//            case "POST":
//                ReviewForm form = new ReviewForm(request);
//
//                String action = request.getParameter("action");
//
//                if (action != null && action.equals("submit")) {
//                    Optional<User> user = UserDao.getInstance().fromSession(request.getSession());
//                    if (!user.isPresent()) {
//                        response.setStatus(401);
//                        request.setAttribute(
//                                "alert",
//                                new Alert("danger", "You must be logged in to create a review.")
//                        );
//                        response.sendRedirect(request.getContextPath() + "/login");
//                        return;
//                    }
//                    Validation v = form.validate();
//
//                    if (v.isValid()) {
//                        Review reviews = new Review();
//                        reviews.setUserId(user.get().getId());
//                        reviews.setName(form.getName());
//                        reviews.setDescription(form.getDescription());
//                        try {
//                            ReviewDao.getInstance().create(reviews);
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        // TODO: redirect to location page
//
//                        response.sendRedirect(request.getContextPath() + "/reviews");
//                        return;
//                    } else {
//                        request.setAttribute("errors", v.getMessages());
//                    }
//                }
//
//                request.setAttribute("form", form);
//
//                request.getRequestDispatcher("/template/reviews/form.jsp").forward(request, response);
//                break;
//        }
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = (User) request.getAttribute( "user");

        Long amenityId = Util.parseLongOrNull(request.getParameter("amenityId"));

        if (request.getMethod().equals("POST")) {
            Review review = new Review();
            review.setUserId(user.getId());

            review.setAmenityId(amenityId);

            review.setName(request.getParameter("title"));
            review.setDescription(request.getParameter("description"));

            ReviewDao.create(review);

        }

        Optional<Amenity> amenity = AmenityDao.getInstance().get(amenityId);

        if (amenity.isPresent()){

            AmenityTypeMetricDao.getInstance().getAllByAmenityType(amenity.get().getAmenityTypeId());

        }

        request.getRequestDispatcher("/template/reviews/index.jsp").forward(request, response);
    }
}
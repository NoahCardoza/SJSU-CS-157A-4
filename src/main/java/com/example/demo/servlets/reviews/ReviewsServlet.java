package com.example.demo.servlets.reviews;

import com.example.demo.Util;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.*;
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
                case "view":
                    view(request, response);
                    break;
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

    public void view(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        Long reviewId = Util.parseLongOrNull(request.getParameter("id"));

        if (reviewId == null) {
            response.sendRedirect(request.getContextPath() + "/reviews");
            return;
        }

        Optional<Review> review = ReviewDao.get(reviewId);

        if (!review.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/reviews");
            return;
        }

        List<AmenityTypeMetricRecordWithName> metrics =  ReviewDao.getAllReviewMetricRecordsWithNames(reviewId);

        response.getWriter().println(review.get());
        response.getWriter().println(metrics);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = (User) request.getAttribute( "user");

        if (user == null) {
            request.setAttribute("alert", new Alert("danger", "You must be logged in to create a review"));
        }

        Long amenityId = Util.parseLongOrNull(request.getParameter("amenityId"));

        if (amenityId == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Optional<Amenity> amenity = AmenityDao.getInstance().get(amenityId);

        if (!amenity.isPresent()){
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<AmenityTypeMetric> metrics = AmenityTypeMetricDao.getInstance().getAllByAmenityType(amenity.get().getAmenityTypeId());
        request.setAttribute("metrics", metrics);

        if (request.getMethod().equals("POST")) {
            Review review = new Review();
            review.setUserId(user.getId());

            review.setAmenityId(amenityId);

            review.setName(request.getParameter("title"));
            review.setDescription(request.getParameter("description"));

            ReviewDao.create(review);

            for (AmenityTypeMetric metric : metrics) {
                AmenityTypeMetricRecord metricRecord = new AmenityTypeMetricRecord();
                metricRecord.setReviewId(review.getId());
                metricRecord.setAmenityMetricId(metric.getId());
                int value = Util.parseIntOrDefault(request.getParameter("metric-" + metric.getId()), 0);

                if (value < 0) {
                    value = 0;
                } else if (value > 5) {
                    value = 5;
                }

                metricRecord.setValue(value);

                ReviewDao.createReviewRecord(metricRecord);
            }

            response.sendRedirect(request.getContextPath() + "/reviews?f=view&id=" + review.getId());

            return;
        }

        request.getRequestDispatcher("/template/reviews/create.jsp").forward(request, response);
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

    }
}
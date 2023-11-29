package com.example.demo.servlets.users;

import com.example.demo.Emailer;
import com.example.demo.Guard;
import com.example.demo.S3;
import com.example.demo.Util;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.*;
import com.example.demo.daos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "Users", value = "/users")
public class UsersServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doRequest(request, response);
    }
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String function = request.getParameter("f");

        if (function == null) {
            function = "index";
        }

        try {
            switch (function) {
                case "get" -> get(request, response);
                case "edit" -> edit(request, response);
                default -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendRedirect(request.getContextPath() + "/");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.getSession().setAttribute("alert", new Alert("danger", "An unexpected error occurred."));
            response.sendRedirect(request.getContextPath() + "/");
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    public void get(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        Long profileUserId = Guard.getLongParameter(
                request,
                response,
                "id"
        );

        if (profileUserId == null) return;

        Optional<User> profile = UserDao.getInstance().get(profileUserId);

        if (profile.isEmpty()) {
            Guard.redirectToHome(
                    request,
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    new Alert("danger", "User not found.")
            );
            return;
        }

        request.setAttribute("profile", profile.get());
        request.setAttribute("stats", UserDao.getInstance().getUserStats(profile.get()));

        User user = (User) request.getAttribute("user");
        List<Review> reviews = ReviewDao.getInstance().getReviewsByUser(
                profile.get(),
                user != null ? user.getId() : null,
                user == null ? Boolean.FALSE : user.isAdministrator() || user.isModerator()
        );

        for (Review review: reviews) {
            review.setMetrics(ReviewDao.getAllReviewMetricRecordsWithNames(review.getId()));
            review.setImages(ReviewDao.getAllImagesForReview(review.getId()));
            UserDao.getInstance().get(review.getUserId()).ifPresent(review::setUser);
        }

        request.setAttribute("reviews", reviews);
        request.getRequestDispatcher("/template/users/get.jsp").forward(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = Guard.requireAuthenticationWithMessage(
                request,
                response,
                "You must be logged in to edit your profile."
        );

        if (user == null) return;

        Long userId = Guard.getLongParameter(
                request,
                response,
                "id"
        );

        if (userId == null) return;

        Optional<User> profile = UserDao.getInstance().get(userId);

        if (profile.isEmpty()) {
            Guard.redirectToHome(
                    request,
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    new Alert("danger", "User not found.")
            );
            return;
        }

        if (!user.getId().equals(profile.get().getId())) {
            Guard.redirectTo(
                    request,
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "/users?f=get&id=" + profile.get().getId(),
                    new Alert("danger", "You can only edit your own profile.")
            );
            return;
        }

        if (profile.get().isBanned()) {
            Guard.redirectTo(
                    request,
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "/users?f=get&id=" + profile.get().getId(),
                    new Alert("danger", "You cannot edit your profile while banned.")
            );
            return;
        }

        request.setAttribute("profile", profile.get());

        request.getRequestDispatcher("/template/users/form.jsp").forward(request, response);
    }
}
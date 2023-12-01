package com.example.demo.servlets.users;

import com.example.demo.*;
import com.example.demo.beans.Alert;
import com.example.demo.beans.entities.*;
import com.example.demo.beans.forms.UserForm;
import com.example.demo.daos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

        if (request.getMethod().equals("POST")) {
            User user = Guard.requireAuthenticationWithMessage(
                    request,
                    response,
                    "You must be logged in to edit your profile."
            );

            if (user == null) return;

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

            UserForm form = new UserForm(request);
            request.setAttribute("form", form);

            String errorMessage = form.validate();
            if (errorMessage != null) {
                request.setAttribute("alert", new Alert("danger", errorMessage));
                request.getRequestDispatcher("/template/users/get.jsp").forward(request, response);
                return;
            }

            profile.get().setUsername(form.getUsername());
            profile.get().setName(form.getName());
            profile.get().setPrivateProfile(form.getPrivateProfile());
            System.out.println(profile.get());
            if (form.isPasswordChanged()) {
                if (Security.checkPassword(form.getOldPassword(), profile.get().getPassword())) {
                    profile.get().setPassword(Security.hashPassword(form.getNewPassword()));
                } else {
                    request.setAttribute("alert", new Alert("danger", "Old password is incorrect."));
                    request.getRequestDispatcher("/template/users/get.jsp").forward(request, response);
                    return;
                }
            }
            try {
                UserDao.getInstance().update(profile.get());
            } catch (SQLException e) {
                request.setAttribute(
                        "alert",
                        new Alert(
                                "danger",
                                switch (e.getErrorCode()) {
                                    case 1062 -> "Username or name is already in use.";
                                    case 1406 -> "A field is too long.";
                                    case 1048 -> "A required field is missing.";
                                    default -> "An unexpected error occurred.";
                                }
                        )
                );
                e.printStackTrace();
                request.getRequestDispatcher("/template/users/get.jsp").forward(request, response);
                return;
            }
            request.setAttribute("user", profile.get());
            request.setAttribute("alert", new Alert("success", "Profile updated."));
            request.setAttribute("form", profile.get());
        } else {
            request.setAttribute("form", profile.get());
        }

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
}
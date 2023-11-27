package com.example.demo.servlets.moderation;

import com.example.demo.Emailer;
import com.example.demo.Guard;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "Moderation", value = "/moderation")
public class ModerationServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        doRequest(request, response);
    }

    public void doRequest(HttpServletRequest request, HttpServletResponse response) {
        String function = request.getParameter("f");

        if (function == null) {
            function = "users";
        }

        try {
            switch (function) {
                case "users":
                    users(request, response);
                    break;
                default:
                    index(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        response.sendRedirect(request.getContextPath() + "/moderation?f=users");
    }

    private void users(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, InterruptedException {
        User currentUser = Guard.requireAuthenticationWithMessage(request, response, "You must be logged in to view the moderation panel.");
        if (currentUser == null) {
            return;
        }
        String query = request.getParameter("q");

        if (request.getMethod().equals("POST")) {
            Long userId = Long.parseLong(request.getParameter("user_id"));

            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/moderation?f=users");
                return;
            }

            Optional<User> user = UserDao.getInstance().get(userId);

            if (!user.isPresent()) {
                response.sendRedirect(request.getContextPath() + "/moderation?f=users");
                return;
            }

            String action = request.getParameter("action");

            switch (action) {
                case "ban" -> {
                    if (currentUser.getId().equals(userId)) {
                        request.setAttribute("alert", new Alert("danger", "You cannot ban yourself."));
                        break;
                    }
                    if (user.get().isAdministrator() && !currentUser.getId().equals(1L)) {
                        request.setAttribute("alert", new Alert("danger", "The super-admin is the only one who can ban other admins."));
                        break;
                    }

                    UserDao.getInstance().ban(userId);
                    Emailer.sendBanNotice(user.get());
                }
                case "unban" -> UserDao.getInstance().unban(userId);
                case "role_change" -> {
                    Integer role = Util.parseIntOrNull(request.getParameter("role"));
                    if (role == null) {
                        response.sendRedirect(request.getContextPath() + "/moderation?f=users");
                        return;
                    }
                    if (currentUser.getId().equals(userId)) {
                        request.setAttribute("alert", new Alert("danger", "You cannot change your own role."));
                        break;
                    }
                    if (user.get().isAdministrator() && !currentUser.getId().equals(1L)) {
                        request.setAttribute("alert", new Alert("danger", "The super-admin is the only one who can change the role of other admins."));
                        break;
                    }

                    // TODO: verify role changes are allowed
                    //       for now we are trusting that admins/mods
                    //       play fair

                    // TODO: remove flags in favor of string roles "admin" and "moderator"

                    switch (role) {
                        case 1 -> {
                            UserDao.getInstance().demote(userId, "administrator");
                            UserDao.getInstance().demote(userId, "moderator");
                        }
                        case 2 -> {
                            UserDao.getInstance().promote(userId, "moderator");
                            UserDao.getInstance().demote(userId, "administrator");
                        }
                        case 3 -> {
                            UserDao.getInstance().demote(userId, "moderator");
                            UserDao.getInstance().promote(userId, "administrator");
                        }
                        default -> {
                            request.setAttribute("alert", new Alert("danger", "Invalid role."));
                        }
                    }
                }
            }
        }
        List<User> users;
        if (query == null) {
            users = UserDao.getInstance().getAll();
        } else {
            users = UserDao.getInstance().fuzzySearch(request.getParameter("q"));
        }

        request.setAttribute("users", users);

        request.getRequestDispatcher("template/moderation/users.jsp").forward(request, response);

    }
}
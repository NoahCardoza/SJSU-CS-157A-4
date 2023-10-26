package com.example.demo.servlets.locations;

import com.example.demo.Validation;
import com.example.demo.beans.Alert;
import com.example.demo.beans.Location;
import com.example.demo.beans.User;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.LocationDao;
import com.example.demo.daos.UserDao;
import com.example.demo.servlets.DatabaseHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "New Location", value = "/location/new")
public class NewLocation extends DatabaseHttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<Location> locations = LocationDao.getInstance().getParentLocationsOf(null);

            for (int i = 0; i < locations.size(); i++) {
                System.out.println(locations.get(i).getId());
            }

            request.setAttribute(
                    "hasParent", false
            );

            request.setAttribute(
                    "locations",
                    locations
            );

            request.getRequestDispatcher("/template/locations/new-location.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
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

                response.sendRedirect(request.getContextPath() + "/locations");
                return;
            } else {
                request.setAttribute("errors", v.getMessages());
            }
        }

        request.setAttribute("form", form);

        request.getRequestDispatcher("/template/locations/new-location.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
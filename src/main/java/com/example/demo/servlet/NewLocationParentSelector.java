package com.example.demo.servlet;

import com.example.demo.bean.LocationForm;
import com.example.demo.orm.Location;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "New Location Parent Selector", value = "/location/new/parent")
public class NewLocationParentSelector extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            LocationForm form = new LocationForm(request);

            // if parent id is 0, it means that the user has selected "None"
            if (form.getParentId() != null && form.getParentId() == 0) {
                form.setParentId(null);
            }

            String action = request.getParameter("action");

            // if the user clicks the back button, we need to get the parent of the current parent
            if (action != null && action.equals("back") && form.getParentId() != null) {
                Location parentLocation = Location.getLocationById(form.getParentId());
                if (parentLocation != null) {
                    form.setParentId(parentLocation.getParentLocationId());
                    if (parentLocation.getParentLocationId() == null) {
                        // if the parent of the parent is null, it means that the parent is the root
                        form.setParentName("None");
                    } else {
                        // get the name of the parent
                        parentLocation = Location.getLocationById(parentLocation.getParentLocationId());
                        if (parentLocation != null) {
                            form.setParentName(parentLocation.getName());
                        } else {
                            // just to be safe, this shouldn't happen unless something is removed
                            // from the database while the user is using the app
                            form.setParentId(null);
                            form.setParentName("None");
                        }
                    }
                }

            }

            List<Location> locations = Location.getParentLocationsOf(form.getParentId());

            // if a parent is selected, add it to the list of locations
            // so the user can see it
            if (form.getParentId() != null) {
                Location selected = new Location();

                selected.setId(form.getParentId());
                selected.setName(form.getParentName());

                locations.add(0, selected);
            }

            request.setAttribute(
                    "locations",
                    locations
            );

            request.setAttribute("form", form);

            request.getRequestDispatcher("/template/new-location-parent.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
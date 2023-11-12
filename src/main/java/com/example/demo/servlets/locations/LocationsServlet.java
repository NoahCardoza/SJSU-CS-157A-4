package com.example.demo.servlets.locations;

import com.example.demo.Database;
import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.*;
import com.example.demo.beans.entities.AmenityWithImage;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.Revision;
import com.example.demo.beans.entities.User;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.AmenityDao;
import com.example.demo.daos.LocationDao;
import com.example.demo.daos.RevisionDao;
import com.example.demo.daos.UserDao;
import com.lambdaworks.crypto.SCryptUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "Locations", value = "/locations")
public class LocationsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        try {
            doRequest(request, response);
        } catch (SQLException e) {
            response.setStatus(500);
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        try {
            doRequest(request, response);
        } catch (SQLException e) {
            response.setStatus(500);
            throw new RuntimeException(e);
        }
    }

    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws SQLException {
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
                case "map":
                    map(request, response);
                    break;
                case "edit":
                    edit(request, response);
                    break;
                case "parentSelect":
                    parentSelect(request, response);
                    break;
                case "delete":
                    request.getRequestDispatcher("/template/locations/delete-location.jsp").forward(request, response);
                    break;
                default:
                    getAll(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void map(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long locationId = Util.parseLongOrNull(request.getParameter("id"));
            if (locationId == null) {
                response.setStatus(400);
                response.getWriter().write("Location ID is required");
                return;
            }

            Location location = LocationDao.getInstance().get(locationId).orElse(null);

            if (location == null) {
                response.setStatus(404);
                response.getWriter().write("Location not found");
                return;
            }


            Integer width = Util.parseIntOrDefault(request.getParameter("width"), 200);
            Integer height = Util.parseIntOrDefault(request.getParameter("height"), 200);

            String apiKey = System.getProperty("GEOAPIFY_API_KEY");

            URL url = new URL("https://maps.geoapify.com/v1/staticmap?style=osm-carto&" +
                    "width=" + width +
                    "&height=" + height +
                    "&center=lonlat:" + location.getLatitude() + "," + location.getLongitude() +
                    "&marker=lonlat:" + location.getLatitude() + "," + location.getLongitude() + ";" +
                    "color:%23ff0000;size:medium&zoom=14&" +
                    "apiKey=" + apiKey
            );

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            response.setContentType("image/jpeg");

            inputStream.transferTo(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void get(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        Long locationId = Util.parseLongOrNull(request.getParameter("id"));

        if (locationId == null) {
            response.sendRedirect(request.getContextPath() + "/locations");
            return;
        }

        Optional<Location> location = LocationDao.getInstance().get(locationId);

        if (location.isPresent()) {
            request.setAttribute(
                    "location",
                    location.get()
            );
        } else {
            System.out.println("Location not found");
            response.sendRedirect(request.getContextPath() + "/locations");
            return;
        }

        List<Revision> revisions = RevisionDao.getInstance().getRevisionsForLocation(
                locationId,
                (Long) request.getSession().getAttribute("userId")
        );

        request.setAttribute(
                "revisions",
                revisions
        );

        List<AmenityWithImage> amenities = AmenityDao.getInstance().getFromLocationId(locationId);

        request.setAttribute(
                "amenities",
                amenities
        );

        request.getRequestDispatcher("template/locations/get.jsp").forward(request, response);
    }
    public void parentSelect(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        LocationForm form = new LocationForm(request);

        System.out.println(form);

        // if parent id is 0, it means that the user has selected "None"
        if (form.getParentId() != null && form.getParentId() == 0) {
            form.setParentId(null);
        }

        String action = request.getParameter("action");

        // if the user clicks the back button, we need to get the parent of the current parent
        if (action != null && action.equals("back") && form.getParentId() != null) {
            Optional<Location> parentLocationOpt = LocationDao.getInstance().get(form.getParentId());
            if (parentLocationOpt.isPresent()) {
                form.setParentId(parentLocationOpt.get().getParentLocationId());
                if (parentLocationOpt.get().getParentLocationId() == null) {
                    // if the parent of the parent is null, it means that the parent is the root
                    form.setParentName("None");
                } else {
                    // get the name of the parent
                    parentLocationOpt = LocationDao.getInstance().get(parentLocationOpt.get().getParentLocationId());
                    if (parentLocationOpt.isPresent()) {
                        form.setParentName(parentLocationOpt.get().getName());
                    } else {
                        // just to be safe, this shouldn't happen unless something is removed
                        // from the database while the user is using the app
                        form.setParentId(null);
                        form.setParentName("None");
                    }
                }
            }

        }

        List<Location> locations = LocationDao.getInstance().getParentLocationsOf(form.getParentId());

        Long locationId = Util.parseLongOrNull(request.getParameter("id"));

        if (locationId == null) {
            response.sendRedirect(request.getContextPath() + "/locations");
            return;
        }

        // remove the current location from the list of locations
        locations = locations.stream().filter(
                (location) -> !location.getId().equals(locationId)
        ).collect(Collectors.toList());


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
        System.out.println(form);

        request.getRequestDispatcher("/template/locations/parent-select.jsp").forward(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        Long locationId = Util.parseLongOrNull(request.getParameter("id"));
        if (locationId == null) {
            response.sendRedirect(request.getContextPath() + "/locations");
            return;
        }

        Optional<Location> location = LocationDao.getInstance().get(locationId);

        if (!location.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/locations");
            return;
        }


        LocationForm form;
        if (request.getMethod().equals("GET")) {
            form = new LocationForm(location.get());
            form.setParentName("None");

            if (location.get().getParentLocationId() != null) {
                Optional<Location> parentLocation = LocationDao.getInstance().get(location.get().getParentLocationId());
                parentLocation.ifPresent(value -> form.setParentName(value.getName()));
            }
        } else {
            form = new LocationForm(request);

            String action = request.getParameter("action");
            if (action != null && action.equals("submit")) {
                Optional<User> user = UserDao.getInstance().fromSession(request.getSession());
                if (user.isEmpty()) {
                    response.setStatus(401);
                    request.getSession(true).setAttribute(
                            "alert",
                            new Alert("danger", "You must be logged in to edit a location.")
                    );
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                Validation v = form.validate();

                if (v.isValid()) {
                    Location newLocation = new Location();
                    newLocation.setId(locationId);
                    newLocation.setUserId(user.get().getId());
                    newLocation.setName(form.getName());
                    newLocation.setDescription(form.getDescription());
                    newLocation.setAddress(form.getAddress());
                    newLocation.setLatitude(form.getLatitude());
                    newLocation.setLongitude(form.getLongitude());
                    newLocation.setParentLocationId(form.getParentId());

                    // TODO: implement diff and track changes
                    LocationDao.getInstance().update(newLocation);

                    RevisionDao.getInstance().createRevisionForLocationEdit(user.get().getId(), location.get(), newLocation);


                    response.sendRedirect(request.getContextPath() + "/locations?f=get&id=" + locationId);
                    return;
                } else {
                    // TODO: implement form errors and a list of alerts
                    request.setAttribute("alert", new Alert("danger", v.getMessages().get(0)));
                    System.out.println(v.getMessages());
                }
            }
        }

        request.setAttribute("form", form);
        request.setAttribute("primaryButtonText", "Update");
        request.setAttribute("headerText", "Update Location");

        request.getRequestDispatcher("/template/locations/form.jsp").forward(request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        request.setAttribute("headerText", "Create Location");
        request.setAttribute("primaryButtonText", "Create");

        switch (request.getMethod()) {
            case "GET":
                List<Location> locations = LocationDao.getInstance().getParentLocationsOf(null);

                request.setAttribute("hasParent", false);
                request.setAttribute("locations", locations);

                request.getRequestDispatcher("/template/locations/form.jsp").forward(request, response);
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
                            request.setAttribute("alert", new Alert("danger", "An error occurred while creating the location."));
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

                request.getRequestDispatcher("/template/locations/form.jsp").forward(request, response);
                break;
        }
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Location> locations = LocationDao.getInstance().getAll();

        request.setAttribute(
                "locations",
                locations
        );

        request.getRequestDispatcher("template/locations/index.jsp").forward(request, response);
    }
}
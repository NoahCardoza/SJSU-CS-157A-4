package com.example.demo.servlets.locations;

import com.example.demo.*;
import com.example.demo.beans.*;
import com.example.demo.beans.entities.*;
import com.example.demo.beans.forms.LocationForm;
import com.example.demo.daos.AmenityDao;
import com.example.demo.daos.LocationDao;
import com.example.demo.daos.RevisionDao;
import com.example.demo.daos.UserDao;
import com.example.demo.servlets.search.SearchServlet;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "Locations", value = "/locations")
//@MultipartConfig
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
                    break;
                case "create":
                    create(request, response);
                    break;
                case "ajax":
                    ajax(request, response);
                    break;
                case "map":
                    SearchServlet.setSearchAttributes(request, null);
                    request.getRequestDispatcher("/template/locations/map.jsp").forward(request, response);
                    break;
                case "mapImage":
                    mapImage(request, response);
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

    private void ajax(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        if (!request.getMethod().equals("POST")) {
            response.setStatus(400);
            response.getWriter().write("Only POST requests are allowed");
            return;
        }

        String method = request.getParameter("m");

        if (method == null) {
            response.setStatus(400);
            response.getWriter().write("Method is required");
            return;
        }

        Double longitude = Util.parseDoubleOrNull(request.getParameter("longitude"));
        Double latitude = Util.parseDoubleOrNull(request.getParameter("latitude"));
        Integer radius = Util.parseIntOrNull(request.getParameter("radius"));

        if (longitude == null || latitude == null || radius == null) {
            response.setStatus(400);
            return;
        }

        List<Location> locations;
        Gson gson = new Gson();

        switch (method) {
            case "locationsWithinDistance":
                locations = LocationDao.getInstance().search(longitude, latitude, radius);
                response.getWriter().write(gson.toJson(locations));
                response.setStatus(200);
                break;
            case "search":
                locations = LocationDao.getInstance().search(longitude, latitude, radius);
                SearchServlet.setSearchAttributes(request, locations);

                String formHtml = Util.captureTemplateOutput(request, response, "/template/locations/ajaxForm.jsp");

                List<Amenity> amenities = (List<Amenity>) request.getAttribute("amenities");

                // group amenities by location id
                HashMap<Long, List<Amenity>> locationIds = new HashMap<>();
                for (Amenity amenity : amenities) {
                    if (!locationIds.containsKey(amenity.getLocationId())) {
                        locationIds.put(amenity.getLocationId(), new ArrayList<>());
                    }
                    locationIds.get(amenity.getLocationId()).add(amenity);
                }

                for (Location location : locations) {
                    if (locationIds.containsKey(location.getId())) {
                        location.setAmenities(locationIds.get(location.getId()));
                    } else {
                        location.setAmenities(new ArrayList<>());
                    }
                }
                response.getWriter().write(gson.toJson(new LocationSearchAjaxResponse(
                        formHtml,
                        locations
                )));
                response.setStatus(200);
                break;
            default:
                response.setStatus(400);
                response.getWriter().write("Invalid method");
                break;
        }
    }

    protected void mapImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long locationId = Util.parseLongOrNull(request.getParameter("id"));
            Double latitude = Util.parseDoubleOrNull(request.getParameter("latitude"));
            Double longitude = Util.parseDoubleOrNull(request.getParameter("longitude"));
            User user = (User) request.getAttribute("user");

            // only allow users to get the map image of locations
            // or, make sure they are logged in to use custom coordinates
            // this is to prevent abuse of the API key
            // TODO: sign jwt to prevent abuse
            if (user == null) {
                if (locationId == null) {
                    response.setStatus(400);
                    response.getWriter().write("Location ID is required");
                    return;
                }
            }

            if (locationId != null) {
                Location location = LocationDao.getInstance().get(locationId).orElse(null);

                if (location == null) {
                    response.setStatus(404);
                    response.getWriter().write("Location not found");
                    return;
                }

                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            if (latitude == null || longitude == null) {
                response.setStatus(400);
                response.getWriter().write("Latitude and longitude are required");
                return;
            }

            Integer width = Util.parseIntOrDefault(request.getParameter("width"), 200);
            Integer height = Util.parseIntOrDefault(request.getParameter("height"), 200);

            String apiKey = Env.get("GEOAPIFY_API_KEY");

            URL url = new URL("https://maps.geoapify.com/v1/staticmap?style=osm-carto&" +
                    "width=" + width +
                    "&height=" + height +
                    "&center=lonlat:" + longitude + "," + latitude +
                    "&marker=lonlat:" + longitude + "," + latitude + ";" +
                    "color:%23ff0000;size:medium&zoom=14&" +
                    "apiKey=" + apiKey
            );

            response.setHeader("Cache-Control", "max-age=86400");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            response.setContentType("image/jpeg");

            inputStream.transferTo(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            response.setStatus(200);
            response.flushBuffer();
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
        amenities = amenities.subList(0, Math.min(amenities.size(), 10));
        request.setAttribute(
                "amenities",
                amenities
        );

        request.getRequestDispatcher("template/locations/get.jsp").forward(request, response);
    }

    public void parentSelect(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        LocationForm form = new LocationForm(request);

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

        Long locationId = Util.parseLongOrDefault(request.getParameter("id"), 0L);

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
                    Guard.redirectToLogin(
                            request,
                            response,
                            new Alert("danger", "You must be logged in to edit a location.")
                    );
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
                }
            }
        }

        request.setAttribute("form", form);
        request.setAttribute("primaryButtonText", "Update");
        request.setAttribute("titleText", "Update");
        request.setAttribute("headerText", "Update Location");
        request.setAttribute("editMode", true);


        request.getRequestDispatcher("/template/locations/form.jsp").forward(request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        User user = Guard.requireAuthenticationWithMessage(request, response, "You must be logged in to create a location.");
        if (user == null) {
            return;
        }

        request.setAttribute("headerText", "Create Location");
        request.setAttribute("primaryButtonText", "Create");
        request.setAttribute("titleText", "Create");

        LocationForm form = new LocationForm(request);

        switch (request.getMethod()) {
            case "GET":
                List<Location> locations = LocationDao.getInstance().getParentLocationsOf(null);

                request.setAttribute("hasParent", false);
                request.setAttribute("locations", locations);
                request.setAttribute("form", form);

                request.getRequestDispatcher("/template/locations/form.jsp").forward(request, response);
                break;
            case "POST":
                String action = request.getParameter("action");
                if (action != null) {
                    if (action.equals("submit")) {
                        Validation v = form.validate();
                        if (v.isValid()) {
                            Location location = new Location();
                            location.setUserId(user.getId());
                            location.setName(form.getName());
                            location.setDescription(form.getDescription());
                            location.setAddress(form.getAddress());
                            location.setLatitude(form.getLatitude());
                            location.setLongitude(form.getLongitude());
                            location.setParentLocationId(form.getParentId());

                            String tempLocationId = UUID.randomUUID().toString();
                            HttpSession session = request.getSession();
                            session.setAttribute("temp_location_" + tempLocationId, location);
                            response.sendRedirect(request.getContextPath() + "/amenities?f=create&session=" + tempLocationId);
                            return;
                        } else {
                            // TODO: send all errors
                            request.setAttribute("alert", new Alert("danger", v.getMessages().get(0)));
                        }
                    } else {
                        request.setAttribute("alert", new Alert("danger", "Invalid action"));
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
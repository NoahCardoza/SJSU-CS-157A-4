package com.example.demo.servlets.admin;

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

@WebServlet(name = "Admin", value = "/admin")
public class AdminServlet extends HttpServlet {
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
                case "amenityTypeCreate":
                    amenityTypeCreate(request, response);
                    break;
                case "amenityTypeEdit":
                    amenityTypeEdit(request, response);
                    break;
                case "amenityTypeDelete":
                    amenityTypeDelete(request, response);
                    break;
                default:
                    index(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void amenityTypeDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!request.getMethod().equals("POST")) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        Long amenityTypeId = Util.parseLongOrNull(request.getParameter("id"));

        if (amenityTypeId == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        try {
            AmenityTypeDao.getInstance().delete(amenityTypeId);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("alert", new Alert("danger", "Failed to delete amenity type."));
        }

        response.sendRedirect(request.getContextPath() + "/admin");
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

        List<AmenityWithImage> amenities = AmenityDao.getInstance().getFromLocationId(locationId);

        request.setAttribute(
                "amenities",
                amenities
        );

        request.getRequestDispatcher("template/locations/get.jsp").forward(request, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
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
                        Guard.redirectToLogin(
                                request,
                                response,
                                new Alert("danger", "You must be logged in to create a location.")
                        );
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

                request.getRequestDispatcher("/template/locations/form.jsp").forward(request, response);
                break;
        }
    }

    public void amenityTypeCreate(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        switch (request.getMethod()) {
            case "GET":
                request.getRequestDispatcher("/template/admin/amenityTypeCreate.jsp").forward(request, response);
                break;
            case "POST":
                amenityTypeCreatePost(request, response);
                break;
        }
    }

    private void amenityTypeCreatePost(HttpServletRequest request, HttpServletResponse response) {
        AmenityType amenityType = new AmenityType();
        amenityType.setName(request.getParameter("name"));
        amenityType.setDescription(request.getParameter("description"));

        String attributes = request.getParameter("attributes");
        String metrics = request.getParameter("metrics");

        // TODO: validate

        try {
            amenityType.setId(AmenityTypeDao.getInstance().create(amenityType));

            List<String> attributeList = Arrays.asList(attributes.split(","));

            for(String attribute:attributeList){
                List<String> attributeWithType = Arrays.asList(attribute.split(":"));

                AmenityTypeAttribute newAttribute = new AmenityTypeAttribute();
                newAttribute.setName(attributeWithType.get(0));
                newAttribute.setAmenityTypeId(amenityType.getId());
                newAttribute.setType(attributeWithType.get(1));
                AmenityTypeAttributeDao.getInstance().create(newAttribute);
            }

            List<String> metricList = Arrays.asList(metrics.split(","));
            for(String metric:metricList){
                AmenityTypeMetric newMetric = new AmenityTypeMetric();
                newMetric.setName(metric);
                newMetric.setAmenityTypeId(amenityType.getId());
                AmenityTypeMetricDao.getInstance().create(newMetric);
            }

            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            request.setAttribute("amenityType", amenityType);
            request.setAttribute("alert", new Alert("danger", "Failed to insert amenity type."));
        }
    }



    public void amenityTypeEdit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        switch (request.getMethod()) {
            case "GET":
                amenityTypeEditGet(request, response);
                break;
            case "POST":
                amenityTypeEditPost(request, response);
                break;
        }
    }

    private void amenityTypeEditPost(HttpServletRequest request, HttpServletResponse response) {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(Util.parseLongOrNull(request.getParameter("id")));
        amenityType.setParentAmenityTypeId(Util.parseLongOrNull(request.getParameter("parentAmenityTypeId")));
        amenityType.setName(request.getParameter("name"));
        amenityType.setDescription(request.getParameter("description"));

        // TODO: validate

        try {
            AmenityTypeDao.getInstance().update(amenityType);
            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            request.setAttribute("amenityType", amenityType);
            request.setAttribute("alert", new Alert("danger", "Failed to update amenity type."));
        }
    }

    private void amenityTypeEditGet(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException {
        Long amenityTypeId = Util.parseLongOrNull(request.getParameter("id"));

        if (amenityTypeId == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        Optional<AmenityType> amenityType = AmenityTypeDao.getInstance().get(amenityTypeId);

        if (amenityType.isPresent()) {
            request.setAttribute(
                    "amenityType",
                    amenityType.get()
            );
        } else {
            System.out.println("Amenity type not found");
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        request.getRequestDispatcher("/template/admin/amenityTypeEdit.jsp").forward(request, response);
    }

    public void index(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

        request.setAttribute(
                "amenityTypes",
                amenityTypes
        );

        request.getRequestDispatcher("template/admin/index.jsp").forward(request, response);
    }
}
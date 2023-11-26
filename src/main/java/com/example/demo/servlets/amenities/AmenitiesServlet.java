package com.example.demo.servlets.amenities;

import com.example.demo.Guard;
import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.*;
import com.example.demo.beans.entities.*;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.AmenityType;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.beans.forms.AmenityForm;
import com.example.demo.daos.*;
import com.example.demo.daos.LocationDao;
import com.example.demo.daos.AmenityDao;

import com.example.demo.servlets.search.AmenityFilter;
import com.example.demo.servlets.search.AmenityTypeAttributeGrouper;
import com.example.demo.beans.entities.AmenityWithImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Enumeration;
import java.util.stream.Collectors;

@WebServlet(name = "Amenities", value = "/amenities")

public class AmenitiesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
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
                    edit(request, response);
                    break;
                case "delete":
                    delete(request, response);
                    break;
                case "typeSelect":
                    typeSelect(request, response);
                    break;
                case "locationSelect":
                    locationSelect(request, response);
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

        // request: f: get, id: 1

        // this is the id of the amenity
        Long amenityId = Util.parseLongOrNull(request.getParameter("id"));

        // checking if the id is valid
        if (amenityId == null) {
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }

        Optional<Amenity> amenity = AmenityDao.getInstance().get(amenityId);
        Amenity object = amenity.get();

        System.out.println(amenity);

        if (amenity.isPresent()) {
            request.setAttribute(
                    "amenity",
                    amenity.get()
            );
        } else {
            System.out.println("Amenity not found");
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }

        if (object.getAmenityTypeId() != null) {
            List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(object.getAmenityTypeId());

            var amenitiesTypeAttributeGrouper = new AmenitiesTypeAttributeGrouper(object, amenityTypeAttributes);

            request.setAttribute(
                    "amenityTypeAttributes",
                    amenitiesTypeAttributeGrouper
            );

            List<AmenityTypeMetric> amenityTypeMetricsList = AmenityTypeMetricDao.getInstance().getAllByAmenityType(object.getAmenityTypeId());

            var amenityTypeMetrics = new AmenitiesTypeMetricsGroup(object, amenityTypeMetricsList);


            request.setAttribute(
                    "amenityTypeMetrics",
                    amenityTypeMetrics
            );
        }

        // gets all reviews for this amenity
        List<Review> reviews = ReviewDao.getInstance().getAllReviews(object.getId());
        var amenityReview = new AmenitiesReviewGrouper(object, reviews);
        request.setAttribute(
                "reviews",
                amenityReview
        );

        List<String> urls = ReviewDao.getInstance().getAllImages(object.getId());

        request.setAttribute(
                "images",
                urls
        );

        request.getRequestDispatcher("template/amenity/view-amenity.jsp").forward(request, response);
    }


    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = Guard.requireAuthenticationWithMessage(request, response, "You must be logged in to create a location.");
        if (user == null) {
            return;
        }

        AmenityForm form = new AmenityForm(request);

        switch (request.getMethod()) {
            case "GET":
                List<Location> locations = LocationDao.getInstance().getParentLocationsOf(null);
                List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

                System.out.println(form.getTypeId());

                request.setAttribute("hasParent", false);
                request.setAttribute("locations", locations);
                request.setAttribute("amenityTypes", amenityTypes);

                request.setAttribute("form", form);

                request.getRequestDispatcher("/template/amenity/amenityCreate.jsp").forward(request, response);
                break;
            case "POST":
                List<AmenityTypeAttribute> attributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(0L);

                String action = request.getParameter("action");
                if (action != null) {
                    if (action.equals("submit")) {
                        Validation v = form.validate();
                        if (v.isValid()) {
                            Amenity amenity = new Amenity();
                            amenity.setUserId(user.getId());
                            amenity.setName(form.getName());
                            amenity.setDescription(form.getDescription());
                            amenity.setLocationId(form.getLocationId());
                            amenity.setAmenityTypeId(form.getTypeId());

                            try {
                                AmenityDao.getInstance().create(amenity);

                                for (AmenityTypeAttribute attribute : attributes){
                                    AmenityTypeAttributeRecord attributeRecord = new AmenityTypeAttributeRecord();
                                    attributeRecord.setAmenityId(amenity.getId());
                                    attributeRecord.setAmenityAttributeId(attribute.getId());
                                    attributeRecord.setValue(request.getParameter("amenityTypeAttribute-" + attribute.getId()));
                                    AmenityDao.getInstance().createAmenityRecord(attributeRecord);
                                }

                                response.sendRedirect(request.getContextPath() + "/amenities?f=get&id=" + amenity.getId());
                                return;
                            } catch (SQLException e) {
                                request.setAttribute("alert", new Alert("danger", "An error occurred while creating the location."));
                                e.printStackTrace();
                            }
                            return;
                        } else {
                            // TODO: send all errors
                            request.setAttribute("alert", new Alert("danger", v.getMessages().get(0)));
                        }
                    } else {
                        request.setAttribute("alert", new Alert("danger", "Invalid action"));
                    }
                }

                if(form.getTypeId() != null){
                    attributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(form.getTypeId());
                }
                else {
                    attributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(0L);
                }
                request.setAttribute("amenityTypeAttributes", attributes);
                request.setAttribute("form", form);

                request.getRequestDispatcher("/template/amenity/amenityCreate.jsp").forward(request, response);
                break;
        }
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        User user = Guard.requireAuthenticationWithMessage(request, response, "You must be logged in to edit an amenity.");

        if (user == null) {
            return;
        }

        Long amenityID = Util.parseLongOrNull(request.getParameter("id"));
        if (amenityID == null) {
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }

        Optional<Amenity> amenity = AmenityDao.getInstance().get(amenityID);

        if (!amenity.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }

        AmenityForm form;
        if (request.getMethod().equals("GET")) {
            form = new AmenityForm(amenity.get());
            form.setLocationName("None");

            if (amenity.get().getAmenityTypeId() != null) {
                Optional<Location> parentLocation = LocationDao.getInstance().get(amenity.get().getAmenityTypeId());
                parentLocation.ifPresent(value -> form.setLocationName(value.getName()));
            }
        } else {
            form = new AmenityForm(request);

            String action = request.getParameter("action");
            if (action != null && action.equals("submit")) {
                //Optional<User> user = UserDao.getInstance().fromSession(request.getSession());
                /*if (user.isEmpty()) {
                    Guard.redirectToLogin(
                            request,
                            response,
                            new Alert("danger", "You must be logged in to edit a location.")
                    );
                    return;
                }*/
                Validation v = form.validate();

                if (v.isValid()) {
                    Amenity newAmenity = new Amenity();
                    newAmenity.setId(amenityID);
                    //newAmenity.setUserId(user.get().getId());
                    newAmenity.setName(form.getName());
                    newAmenity.setDescription(form.getDescription());

                    // TODO: implement diff and track changes
                    AmenityDao.getInstance().update(newAmenity);

                    response.sendRedirect(request.getContextPath() + "/locations?f=get&id=" + amenityID);
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

        request.getRequestDispatcher("/template/amenity/amenityEdit.jsp").forward(request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        User user = Guard.requireAuthenticationWithMessage(request, response, "You must be logged in to delete an amenity.");

        Long amenityId = Util.parseLongOrNull(request.getParameter("id"));

        if (amenityId == null) {
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }

        Optional<Amenity> amenity = AmenityDao.getInstance().get(amenityId);

        if (amenity.isPresent()) {
            AmenityDao.getInstance().delete(amenityId);
        } else {
            System.out.println("Amenity not found");
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }


        request.getRequestDispatcher("/template/search/index.jsp").forward(request, response);
    }


    public void typeSelect(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        AmenityForm form = new AmenityForm(request);

        // if parent id is 0, it means that the user has selected "None"
        if (form.getTypeId() != null && form.getTypeId() == 0) {
            form.setTypeId(null);
        }

        String action = request.getParameter("action");

        // if the user clicks the back button, we need to get the parent of the current parent
        if (action != null && action.equals("back") && form.getTypeId() != null) {
            Optional<AmenityType> amenityTypeOpt = AmenityTypeDao.getInstance().get(form.getTypeId());
            if (amenityTypeOpt.isPresent()) {
                form.setTypeId(amenityTypeOpt.get().getParentAmenityTypeId());
                if (amenityTypeOpt.get().getParentAmenityTypeId() == null) {
                    // if the parent of the parent is null, it means that the parent is the root
                    form.setTypeName("None");
                } else {
                    // get the name of the parent
                    amenityTypeOpt = AmenityTypeDao.getInstance().get(amenityTypeOpt.get().getParentAmenityTypeId());
                    if (amenityTypeOpt.isPresent()) {
                        form.setTypeName(amenityTypeOpt.get().getName());
                    } else {
                        // just to be safe, this shouldn't happen unless something is removed
                        // from the database while the user is using the app
                        form.setTypeId(null);
                        form.setTypeName("None");
                    }
                }
            }

        }

        List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

        // if a parent is selected, add it to the list of locations
        // so the user can see it
        if (form.getTypeId() != null) {
            AmenityType selected = new AmenityType();

            selected.setId(form.getTypeId());
            selected.setName(form.getTypeName());

            amenityTypes.add(0, selected);

            List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(form.getTypeId());
            request.setAttribute(
                    "amenityTypeAttributes",
                    amenityTypeAttributes
            );
        }

        request.setAttribute(
                "amenityTypes",
                amenityTypes
        );

        request.setAttribute("form", form);



        request.getRequestDispatcher("/template/amenity/type-select.jsp").forward(request, response);
    }

    public void locationSelect(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        AmenityForm form = new AmenityForm(request);

        // if parent id is 0, it means that the user has selected "None"
        if (form.getLocationId() != null && form.getLocationId() == 0) {
            form.setLocationId(null);
        }

        String action = request.getParameter("action");

        // if the user clicks the back button, we need to get the parent of the current parent
        if (action != null && action.equals("back") && form.getLocationId() != null) {
            Optional<Location> locationOpt = LocationDao.getInstance().get(form.getLocationId());
            if (locationOpt.isPresent()) {
                form.setLocationId(locationOpt.get().getParentLocationId());
                if (locationOpt.get().getParentLocationId() == null) {
                    // if the parent of the parent is null, it means that the parent is the root
                    form.setLocationName("None");
                } else {
                    // get the name of the parent
                    locationOpt = LocationDao.getInstance().get(locationOpt.get().getParentLocationId());
                    if (locationOpt.isPresent()) {
                        form.setLocationName(locationOpt.get().getName());
                    } else {
                        // just to be safe, this shouldn't happen unless something is removed
                        // from the database while the user is using the app
                        form.setLocationId(null);
                        form.setLocationName("None");
                    }
                }
            }
        }

        List<Location> locations = LocationDao.getInstance().getParentLocationsOf(form.getLocationId());

        Long locationId = Util.parseLongOrDefault(request.getParameter("id"), 0L);

        // remove the current location from the list of locations
        locations = locations.stream().filter(
                (location) -> !location.getId().equals(locationId)
        ).collect(Collectors.toList());

        // if a parent is selected, add it to the list of locations
        // so the user can see it
        if (form.getLocationId() != null) {
            Location selected = new Location();

            selected.setId(form.getLocationId());
            selected.setName(form.getLocationName());

            locations.add(0, selected);
        }

        request.setAttribute(
                "locations",
                locations
        );

        request.setAttribute("form", form);

        request.getRequestDispatcher("/template/amenity/location-select.jsp").forward(request, response);

    }


    public void getAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Amenity> amenities = AmenityDao.getInstance().getAll();

        request.setAttribute(
                "amenities",
                amenities
        );

        // TODO: change the path later
        request.getRequestDispatcher("template/amenity/view-amenity.jsp").forward(request, response);
    }
}



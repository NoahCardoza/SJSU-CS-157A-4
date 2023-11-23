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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
                case "parentSelect":
                    parentSelect(request, response);
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
        Long amenityId = Util.parseLongOrNull(request.getParameter("id"));

        if (amenityId == null) {
            response.sendRedirect(request.getContextPath() + "/amenities");
            return;
        }

        Optional<Amenity> amenity = AmenityDao.getInstance().get(amenityId);

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

        AmenityFilter amenityFilter = new AmenityFilter(request);

        if (amenityFilter.getAmenityTypeId() != null) {
            List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(amenityFilter.getAmenityTypeId());

            var amenityTypeAttributeGrouper = new AmenityTypeAttributeGrouper(request, amenityTypeAttributes);

            request.setAttribute(
                    "amenityTypeAttributes",
                    amenityTypeAttributeGrouper
            );
        }

        request.getRequestDispatcher("template/amenity/view-amenity.jsp").forward(request, response);
    }


    public void create(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        User user = Guard.requireAuthenticationWithMessage(request, response, "You must be logged in to create an amenity.");
        if (user == null) {
            return;
        }

        switch (request.getMethod()) {
            case "GET":
                List<Location> locations = LocationDao.getInstance().getAll();

                request.setAttribute(
                        "locations",
                        locations
                );

                List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

                request.setAttribute(
                        "amenityTypes",
                        amenityTypes
                );

                AmenityFilter amenityFilter = new AmenityFilter(request);

                if (amenityFilter.getAmenityTypeId() != null) {

                    List<AmenityTypeAttribute> amenityTypeAttributes = AmenityTypeAttributeDao.getInstance().getAllByAmenityType(amenityFilter.getAmenityTypeId());

                    var amenityTypeAttributeGrouper = new AmenityTypeAttributeGrouper(request, amenityTypeAttributes);

                    request.setAttribute(
                            "amenityTypeAttributes",
                            amenityTypeAttributeGrouper
                    );
                }

                List<AmenityWithImage> amenities = AmenityDao.getInstance().getWithFilter(amenityFilter, null);

                request.setAttribute(
                        "amenities",
                        amenities
                );

                request.getRequestDispatcher("/template/amenity/amenityForm.jsp").forward(request, response);
                break;

            case "POST":
                AmenityForm form = new AmenityForm(request);

                String action = request.getParameter("action");

                if (action != null && action.equals("submit")) {
                    Validation v = form.validate();

                    if (v.isValid()) {
                        Amenity amenity = new Amenity();
                        amenity.setUserId(user.getId());
                        amenity.setLocationId(form.getParentId());
                        amenity.setName(form.getName());
                        amenity.setDescription(form.getDescription());
                        amenity.setAmenityTypeId(form.getParentId());
                        try {
                            AmenityDao.getInstance().create(amenity);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        // TODO: redirect to location page

                        response.sendRedirect(request.getContextPath() + "/amenities");
                        return;
                    } else {
                        request.setAttribute("errors", v.getMessages());
                    }
                }

                request.setAttribute("form", form);

                request.getRequestDispatcher("/template/amenity/amenityForm.jsp").forward(request, response);
                break;
        }
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

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
            form.setParentName("None");

            if (amenity.get().getAmenityTypeId() != null) {
                Optional<Location> parentLocation = LocationDao.getInstance().get(amenity.get().getAmenityTypeId());
                parentLocation.ifPresent(value -> form.setParentName(value.getName()));
            }
        } else {
            form = new AmenityForm(request);

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
                    Amenity newAmenity = new Amenity();
                    newAmenity.setId(amenityID);
                    newAmenity.setUserId(user.get().getId());
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

        request.getRequestDispatcher("/template/amenity/amenityForm.jsp").forward(request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        // TODO: Needs to check if user is admin for deletion
        /*Optional<User> user = UserDao.getInstance().isAdmin();
        if (!user.isPresent()) {
            response.setStatus(401);
            request.setAttribute(
                    "alert",
                    new Alert("danger", "You must be an admin to delete an amenity.")
            );
            response.sendRedirect(request.getContextPath() + "/search");
            return;
        }*/

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


    // TODO: ask if this should be parent_amenity_type or just the amentiy type's id
    public void parentSelect(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        AmenityForm form = new AmenityForm(request);

        // if parent id is 0, it means that the user has selected "None"
        if (form.getParentId() != null && form.getParentId() == 0) {
            form.setParentId(null);
        }

        String action = request.getParameter("action");

        // if the user clicks the back button, we need to get the parent of the current parent
        if (action != null && action.equals("back") && form.getParentId() != null) {
            Optional<AmenityType> amenityTypeOpt = AmenityTypeDao.getInstance().get(form.getParentId());
            if (amenityTypeOpt.isPresent()) {
                form.setParentId(amenityTypeOpt.get().getParentAmenityTypeId());
                if (amenityTypeOpt.get().getParentAmenityTypeId() == null) {
                    // if the parent of the parent is null, it means that the parent is the root
                    form.setParentName("None");
                } else {
                    // get the name of the parent
                    amenityTypeOpt = AmenityTypeDao.getInstance().get(amenityTypeOpt.get().getParentAmenityTypeId());
                    if (amenityTypeOpt.isPresent()) {
                        form.setParentName(amenityTypeOpt.get().getName());
                    } else {
                        // just to be safe, this shouldn't happen unless something is removed
                        // from the database while the user is using the app
                        form.setParentId(null);
                        form.setParentName("None");
                    }
                }
            }

        }

        List<AmenityType> amenityTypes = AmenityTypeDao.getInstance().getAll();

        // if a parent is selected, add it to the list of locations
        // so the user can see it
        if (form.getParentId() != null) {
            AmenityType selected = new AmenityType();

            selected.setId(form.getParentId());
            selected.setName(form.getParentName());

            amenityTypes.add(0, selected);
        }

        request.setAttribute(
                "amenityTypes",
                amenityTypes
        );

        request.setAttribute("form", form);

        request.getRequestDispatcher("/template/amenity/amenity-type-select.jsp").forward(request, response);
    }

    public void locationSelect(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        AmenityForm form = new AmenityForm(request);

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

        request.getRequestDispatcher("/template/amenity/location-select.jsp").forward(request, response);

    }


    public void getAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Amenity> amenities = AmenityDao.getInstance().getAll();

        request.setAttribute(
                "amenities",
                amenities
        );

        System.out.println("im in the default method!");

        // TODO: change the path later
        request.getRequestDispatcher("template/auth/signup.jsp").forward(request, response);
    }
}



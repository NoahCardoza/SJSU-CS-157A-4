package com.example.demo.beans.forms;

import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.entities.Amenity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

public class AmenityForm {

    Long typeId = null;
    String typeName = "None";
    Long locationId;
    String locationName = "None";
    String name = "";
    String description = "";


    public AmenityForm() {}

    public AmenityForm(HttpServletRequest request) {
        this.typeId = Util.parseLongOrNull(request.getParameter("typeId"));
        this.locationId = Util.parseLongOrNull(request.getParameter("locationId"));
        this.locationName = request.getParameter("locationName");
        this.typeName = request.getParameter("typeName");
        this.name = request.getParameter("name");
        this.description = request.getParameter("description");
    }

    public AmenityForm(Amenity amenity) {
        this.typeId = amenity.getAmenityTypeId();
        this.name = amenity.getName();
        this.description = amenity.getDescription();
    }

    public Validation validate() {
        Validation v = new Validation();
        if (name == null) {
            v.addMessage("Name is required.");
        }

        if (!v.isValid()) {
            return v;
        }

        name = name.trim();

        if (this.name.isEmpty()) {
            v.addMessage("Name is required.");
        }


        if (name.length() > 255) {
            v.addMessage("Name must be less than 255 characters.");
        }

        if (locationId == null) {
            v.addMessage("Location is required.");
        }

        description = description == null ? "" : description.trim();

        if (description.length() > 255) {
            v.addMessage("Description must be less than 255 characters.");
        }

        return v;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) { this.locationName = locationName; }

    public void setDescription(String description) {
        this.description = description;
    }


}

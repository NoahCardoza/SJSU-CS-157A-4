package com.example.demo.beans.forms;

import com.example.demo.Util;
import com.example.demo.Validation;
import jakarta.servlet.http.HttpServletRequest;

public class LocationForm {
   Integer parentId = null;
   String parentName = "None";

    String name = "";
    String description = "";
    String address = "";

    Double longitude = null;
    Double latitude= null;

    public LocationForm() {}

    public LocationForm(HttpServletRequest request) {
        this.parentId = Util.parseIntOrNull(request.getParameter("parentId"));
        this.parentName = request.getParameter("parentName");
        this.name = request.getParameter("name");
        this.description = request.getParameter("description");
        this.address = request.getParameter("address");
        this.longitude = Util.parseDoubleOrNull(request.getParameter("longitude"));
        this.latitude = Util.parseDoubleOrNull(request.getParameter("latitude"));
    }

    public Validation validate() {
        Validation v = new Validation();
        if (name == null) {
            v.addMessage("Name is required.");
        }

        if (longitude == null) {
            v.addMessage("Longitude is required.");
        }

        if (latitude == null) {
            v.addMessage("Latitude is required.");
        }

        if (!v.isValid()) {
            return v;
        }

        name = name.trim();

        if (this.name.isEmpty()) {
            v.addMessage("Name is required.");
        }

        if (longitude < -180 || longitude > 180) {
            v.addMessage("Longitude must be between -180 and 180.");
        }

        if (latitude < -90 || latitude > 90) {
            v.addMessage("Latitude must be between -90 and 90.");
        }

        if (name.length() > 255) {
            v.addMessage("Name must be less than 255 characters.");
        }

        description = description == null ? "" : description.trim();

        if (description.length() > 255) {
            v.addMessage("Description must be less than 255 characters.");
        }

        address = address == null ? "" : address.trim();

        if (address.length() > 255) {
            v.addMessage("Address must be less than 255 characters.");
        }

        return v;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}

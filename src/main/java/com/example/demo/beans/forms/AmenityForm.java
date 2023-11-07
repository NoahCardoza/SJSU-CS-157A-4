package com.example.demo.beans.forms;

import com.example.demo.Util;
import com.example.demo.Validation;
import com.example.demo.beans.entities.Amenity;
import jakarta.servlet.http.HttpServletRequest;

public class AmenityForm {

    Long parentId = null;
    String parentName = "None";

    String name = "";
    String description = "";


    public AmenityForm() {}

    public AmenityForm(HttpServletRequest request) {
        this.parentId = Util.parseLongOrNull(request.getParameter("parentId"));
        this.parentName = request.getParameter("parentName");
        this.name = request.getParameter("name");
        this.description = request.getParameter("description");
    }

    public AmenityForm(Amenity amenity) {
        this.parentId = amenity.getAmenityTypeId();
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

        description = description == null ? "" : description.trim();

        if (description.length() > 255) {
            v.addMessage("Description must be less than 255 characters.");
        }

        return v;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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


}

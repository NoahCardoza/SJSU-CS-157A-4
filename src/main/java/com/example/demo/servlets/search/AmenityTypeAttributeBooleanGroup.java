package com.example.demo.servlets.search;

import com.example.demo.beans.entities.AmenityTypeAttribute;
import jakarta.servlet.ServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_BOOLEAN_ID;

public class AmenityTypeAttributeBooleanGroup {
    ServletRequest request;
    List<AmenityTypeAttribute> attributes;

    public AmenityTypeAttributeBooleanGroup(ServletRequest request) {
        this.request = request;
        this.attributes = new ArrayList<>();
    }

    public void add(AmenityTypeAttribute attribute) {
        this.attributes.add(attribute);
    }

    @Override
    public String toString() {
        if (attributes.isEmpty()) {
            return "";
        }
        String selected;
        StringJoiner joiner = new StringJoiner("");
        joiner.add(
                "<div class=\"row mb-2\">\n" +
                "   <label for=\""+TYPE_BOOLEAN_ID+"\" class=\"form-label\">Amenity Type</label>\n" +
                "       <select class=\"form-select\" name=\""+TYPE_BOOLEAN_ID+"\" id=\""+TYPE_BOOLEAN_ID+"\" multiple>\n");

        List<String> selectedValues = request.getParameterValues(TYPE_BOOLEAN_ID) == null ? new ArrayList<>() : List.of(request.getParameterValues(TYPE_BOOLEAN_ID));

        for (AmenityTypeAttribute attribute : attributes) {
            selected = selectedValues.contains(attribute.getId().toString()) ? "selected" : "";
            joiner.add("<option " + selected + " value=\"" + attribute.getId() + "\">" + attribute.getName() + "</option>");
        }
        joiner.add("</select></div>");
        return joiner.toString();
    }
}

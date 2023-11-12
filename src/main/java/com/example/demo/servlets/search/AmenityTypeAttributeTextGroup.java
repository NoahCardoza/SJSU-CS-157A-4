package com.example.demo.servlets.search;

import com.example.demo.Database;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.daos.AmenityTypeAttributeDao;
import jakarta.servlet.ServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_BOOLEAN_ID;
import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_TEXT_ID_PREFIX;

public class AmenityTypeAttributeTextGroup {
    ServletRequest request;
    List<AmenityTypeAttribute> attributes;

    public AmenityTypeAttributeTextGroup(ServletRequest request) {
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
        try {
            for (AmenityTypeAttribute attribute : attributes) {
                String formId = TYPE_TEXT_ID_PREFIX + attribute.getId();
                List<String> values = AmenityTypeAttributeDao.getInstance().getAllTextValuesForAttribute(attribute.getId());
                joiner.add(
                        "<div class=\"row mb-2\">\n" +
                        "   <label for=\"" + formId + "\" class=\"form-label\">" + attribute.getName() + "</label>\n" +
                        "       <select class=\"form-select\" name=\"" + formId + "\" id=\"" + formId + "\">\n" +
                                "<option value=\"0\">All</option>");


                List<String> selectedValues = request.getParameterValues(formId) == null ? new ArrayList<>() : List.of(request.getParameterValues(formId));

                for (String value : values) {
                    selected = selectedValues.contains(value) ? "selected" : "";
                    joiner.add("<option " + selected + " value=\"" + value + "\">" + value + "</option>");
                }
                joiner.add("</select></div>");
            }
            return joiner.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

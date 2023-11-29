package com.example.demo.servlets.search;

import com.example.demo.beans.MinMax;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.daos.AmenityTypeAttributeDao;
import jakarta.servlet.ServletRequest;

import java.sql.SQLException;
import java.util.*;

import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_NUMBER_ID_PREFIX;

public class AmenityTypeAttributeNumberGroup {
    private ServletRequest request;
    private List<AmenityTypeAttribute> attributes;

    public AmenityTypeAttributeNumberGroup(ServletRequest request) {
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

        StringJoiner joiner = new StringJoiner("");
        try {
            for (AmenityTypeAttribute attribute : attributes) {
                String formId = TYPE_NUMBER_ID_PREFIX + attribute.getId();
                Optional<MinMax> minMax = AmenityTypeAttributeDao.getInstance().getMinMaxIntValuesForAttribute(attribute.getId());

                if (!minMax.isPresent()) {
                    continue;
                }

                String currentValue = request.getParameter(formId) == null ? "" : request.getParameter(formId);

                joiner.add(
                        "<div class=\"mb-2\">\n" +
                                "   <label for=\"" + formId + "\" class=\"form-label\">" + attribute.getName() + "</label>\n" +
                                "       <select class=\"form-select\" name=\"" + formId + "\" id=\"" + formId + "\">\n" +
                                "<option value=\"0\">All</option>");

                for (int value = minMax.get().getMin(); value <= minMax.get().getMax(); value++) {
                    String selected = currentValue.equals(Integer.toString(value)) ? "selected" : "";
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

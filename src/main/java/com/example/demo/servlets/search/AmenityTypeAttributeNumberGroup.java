package com.example.demo.servlets.search;

import com.example.demo.beans.MinMax;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.daos.AmenityTypeAttributeDao;
import jakarta.servlet.ServletRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_NUMBER_ID_PREFIX;

public class AmenityTypeAttributeNumberGroup {
    ServletRequest request;
    List<AmenityTypeAttribute> attributes;

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
                        "<div class=\"row mb-2\">\n" +
                        "   <label for=\"" + formId + "\" class=\"form-label\">" + attribute.getName() + " ("+ minMax.get().getMin() +" to "+ minMax.get().getMax() +") </label>\n" +
                                "<div class=\"row\">" +
                                "<div class=\"col-10\">" +
                                "<input type=\"range\" class=\"form-range\" id=\""+formId+"\" value=\""+ currentValue +"\" name=\""+formId+"\" min=\""+minMax.get().getMin()+"\" max=\""+minMax.get().getMax()+"\" >\n" +
                                "</div>" +
                                "<div class=\"col-2\">" +
                                "<output id=\""+formId+"-output\"></output>" +
                                "</div>" +
                                "</div>" +
                                "</div>" +
                                "<script>" +
                                "(function(){const input = document.querySelector(\"#"+formId+"\");\n" +
                                "const value = document.querySelector(\"#"+formId+"-output\");\n" +
                                "value.textContent = input.value;\n" +
                                "input.addEventListener(\"input\", (event) => {\n" +
                                "  value.textContent = event.target.value;\n" +
                                "});})()\n" +
                                "</script>"
                );
            }
            return joiner.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

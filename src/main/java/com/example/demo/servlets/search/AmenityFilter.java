package com.example.demo.servlets.search;

import com.example.demo.Util;
import jakarta.servlet.ServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.demo.daos.AmenityTypeAttributeDao.*;

public class AmenityFilter {
    private Long amenityTypeId;
    private HashMap<String, List<String>> numberAttributes;
    private HashMap<String, List<String>> textAttributes;
    private List<String> booleanAttributes;

    public AmenityFilter(ServletRequest request) {
        this.amenityTypeId = Util.nullIfZero(Util.parseLongOrNull(request.getParameter("amenityTypeId")));
        this.numberAttributes = new HashMap<>();
        this.textAttributes = new HashMap<>();
        this.booleanAttributes = request.getParameterValues(TYPE_BOOLEAN_ID) == null ? new ArrayList<>() : List.of(request.getParameterValues(TYPE_BOOLEAN_ID));

        for (String parameterName : request.getParameterMap().keySet()) {
            if (parameterName.startsWith(TYPE_NUMBER_ID_PREFIX)) {
                String attributeId = parameterName.substring(TYPE_NUMBER_ID_PREFIX.length());
                String value = request.getParameter(parameterName);
                if (value != null && !value.isEmpty() && !value.equals("0")) {
                    if (!numberAttributes.containsKey(attributeId)) {
                        numberAttributes.put(attributeId, new ArrayList<>());
                    }
                    numberAttributes.get(attributeId).add(value);
                }
            } else if (parameterName.startsWith(TYPE_TEXT_ID_PREFIX)) {
                String attributeId = parameterName.substring(TYPE_TEXT_ID_PREFIX.length());
                String value = request.getParameter(parameterName);
                if (value != null && !value.isEmpty() && !value.equals("0")) {
                    if (!textAttributes.containsKey(attributeId)) {
                        textAttributes.put(attributeId, new ArrayList<>());
                    }
                    textAttributes.get(attributeId).add(value);
                }
            }
        }
    }

    public Long getAmenityTypeId() {
        return amenityTypeId;
    }

    public HashMap<String, List<String>> getNumberAttributes() {
        return numberAttributes;
    }

    public HashMap<String, List<String>> getTextAttributes() {
        return textAttributes;
    }

    public List<String> getBooleanAttributes() {
        return booleanAttributes;
    }
}

package com.example.demo.servlets.search;

import com.example.demo.beans.entities.AmenityTypeAttribute;
import jakarta.servlet.ServletRequest;

import java.util.ArrayList;
import java.util.List;

public class AmenityTypeAttributeGrouper {
    List<AmenityTypeAttribute> amenityTypeAttributes;
    AmenityTypeAttributeNumberGroup numberAttributes;
    AmenityTypeAttributeTextGroup textAttributes;
    AmenityTypeAttributeBooleanGroup booleanAttributes;


    public AmenityTypeAttributeGrouper(ServletRequest request, List<AmenityTypeAttribute> amenityTypeAttributes) {
        this.amenityTypeAttributes = amenityTypeAttributes;
        this.numberAttributes = new AmenityTypeAttributeNumberGroup(request);
        this.textAttributes = new AmenityTypeAttributeTextGroup(request);
        this.booleanAttributes = new AmenityTypeAttributeBooleanGroup(request);

        for (AmenityTypeAttribute amenityTypeAttribute : amenityTypeAttributes) {
            switch (amenityTypeAttribute.getType()) {
                case "number":
                    this.numberAttributes.add(amenityTypeAttribute);
                    break;
                case "text":
                    this.textAttributes.add(amenityTypeAttribute);
                    break;
                case "boolean":
                    this.booleanAttributes.add(amenityTypeAttribute);
                    break;
            }
        }


    }

    public AmenityTypeAttributeNumberGroup getNumberAttributes() {
        return numberAttributes;
    }

    public AmenityTypeAttributeTextGroup getTextAttributes() {
        return textAttributes;
    }

    public AmenityTypeAttributeBooleanGroup getBooleanAttributes() {
        return booleanAttributes;
    }
}

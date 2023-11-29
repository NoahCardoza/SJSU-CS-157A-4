package com.example.demo.servlets.amenities;

import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.beans.entities.AmenityTypeAttributeRecordWithName;

import java.util.ArrayList;
import java.util.List;

public class AttributeGrouper {

    List<AmenityTypeAttributeRecordWithName> amenityTypeAttributes;
    List<AmenityTypeAttributeRecordWithName> numberAttributes;
    List<AmenityTypeAttributeRecordWithName> textAttributes;
    List<AmenityTypeAttributeRecordWithName> booleanAttributes;


    public AttributeGrouper(List<AmenityTypeAttributeRecordWithName> amenityTypeAttributes) {
        this.amenityTypeAttributes = amenityTypeAttributes;
        this.numberAttributes = new ArrayList<>();
        this.textAttributes = new ArrayList<>();
        this.booleanAttributes = new ArrayList<>();

        for (AmenityTypeAttributeRecordWithName amenityTypeAttribute : amenityTypeAttributes) {
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

    public List<AmenityTypeAttributeRecordWithName> getNumberAttributes() {
        return numberAttributes;
    }

    public List<AmenityTypeAttributeRecordWithName> getTextAttributes() {
        return textAttributes;
    }

    public List<AmenityTypeAttributeRecordWithName> getBooleanAttributes() {
        return booleanAttributes;
    }
}

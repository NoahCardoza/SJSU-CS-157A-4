package com.example.demo.servlets.amenities;

import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import jakarta.servlet.ServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_BOOLEAN_ID;

public class AmenitiesTypeAttributeBooleanGroup {

    Long request;
    List<AmenityTypeAttribute> attributes;

    public AmenitiesTypeAttributeBooleanGroup(Amenity amenity) {
        this.request = amenity.getAmenityTypeId();
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

        String booleanAttributes = "";

        for (AmenityTypeAttribute attribute : attributes) {
            booleanAttributes += attribute + " ";
        }

        return booleanAttributes;
    }
}

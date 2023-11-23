package com.example.demo.servlets.amenities;

import com.example.demo.Database;
import com.example.demo.beans.entities.Amenity;
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

public class AmenitiesTypeAttributeTextGroup {

    Long request;
    List<AmenityTypeAttribute> attributes;

    public AmenitiesTypeAttributeTextGroup(Amenity amenity) {
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

        String textAttributes = "";

        for (AmenityTypeAttribute attribute : attributes) {
            textAttributes += attribute + " ";
        }

        return textAttributes;
    }
}

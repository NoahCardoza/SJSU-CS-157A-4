package com.example.demo.servlets.amenities;

import com.example.demo.beans.MinMax;
import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.daos.AmenityTypeAttributeDao;
import jakarta.servlet.ServletRequest;

import java.sql.SQLException;
import java.util.*;

import static com.example.demo.daos.AmenityTypeAttributeDao.TYPE_NUMBER_ID_PREFIX;

public class AmenitiesTypeAttributeNumberGroup {

    Long request;
    List<AmenityTypeAttribute> attributes;

    public AmenitiesTypeAttributeNumberGroup(Amenity amenity) {

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

        String numberAttributes = "";

        for (AmenityTypeAttribute attribute : attributes) {
            numberAttributes += attribute + " ";
        }

        return numberAttributes;
    }
}

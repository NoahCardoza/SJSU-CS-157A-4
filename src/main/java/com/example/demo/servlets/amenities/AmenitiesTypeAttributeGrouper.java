package com.example.demo.servlets.amenities;

import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.servlets.amenities.AmenitiesTypeAttributeBooleanGroup;
import com.example.demo.servlets.amenities.AmenitiesTypeAttributeNumberGroup;
import com.example.demo.servlets.amenities.AmenitiesTypeAttributeTextGroup;
import jakarta.servlet.ServletRequest;

import java.util.ArrayList;
import java.util.List;
public class AmenitiesTypeAttributeGrouper {

    List<AmenityTypeAttribute> amenityTypeAttributes;
    AmenitiesTypeAttributeNumberGroup numberAttributes;
    AmenitiesTypeAttributeTextGroup textAttributes;
    AmenitiesTypeAttributeBooleanGroup booleanAttributes;


    public AmenitiesTypeAttributeGrouper(Amenity amenity, List<AmenityTypeAttribute> amenityTypeAttributes) {
        this.amenityTypeAttributes = amenityTypeAttributes;
        this.numberAttributes = new AmenitiesTypeAttributeNumberGroup(amenity);
        this.textAttributes = new AmenitiesTypeAttributeTextGroup(amenity);
        this.booleanAttributes = new AmenitiesTypeAttributeBooleanGroup(amenity);

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

    public AmenitiesTypeAttributeNumberGroup getNumberAttributes() {
        return numberAttributes;
    }

    public AmenitiesTypeAttributeTextGroup getTextAttributes() {
        return textAttributes;
    }

    public AmenitiesTypeAttributeBooleanGroup getBooleanAttributes() {
        return booleanAttributes;
    }
}

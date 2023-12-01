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

    Long amenityId;
    Long typeId;
    List<AmenityTypeAttribute> attributes;

    public AmenitiesTypeAttributeTextGroup(Amenity amenity) {
        this.amenityId = amenity.getId();
        this.typeId = amenity.getAmenityTypeId();
        this.attributes = new ArrayList<>();
    }

    public List<AmenityTypeAttribute> getAttributes(){
        return attributes;
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
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        try{
            //get the attribute id and then match it
            for(AmenityTypeAttribute attribute : attributes){
                //gets you the values from the database for the amenityType
                String value = AmenityTypeAttributeDao.getInstance().getAllValuesForAttribute(attribute.getId(), amenityId);

                // may need to change, i'm unsure if there can be multiple values for one attribute
                if(value.isEmpty()){
                    joiner.add(attribute.getName() + ": N/A");
                }
                else {
                    joiner.add(attribute.getName() + ": " + value);
                }
            }

            textAttributes = joiner.toString();

            return textAttributes;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}

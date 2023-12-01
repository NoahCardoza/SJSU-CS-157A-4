package com.example.demo.beans.entities;

import java.io.Serial;
import java.io.Serializable;

public class AmenityTypeAttributeRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Long amenityAttributeId;
    Long amenityId;
    String value;

    public AmenityTypeAttributeRecord() {
        // Default constructor
    }

    public Long getAmenityAttributeId() {return amenityAttributeId; }

    public void setAmenityAttributeId(Long amenityTypeAttributeId) {this.amenityAttributeId = amenityTypeAttributeId;}

    public Long getAmenityId() {return amenityId;}

    public void setAmenityId(Long amenityTypeId){this.amenityId = amenityTypeId;}

    public String getValue(){return value;}

    public void setValue(String value){this.value = value;}

    @Override
    public String toString() {
        return "AmenityTypeAttributeRecord{" +
                "amenityAttributeId=" + amenityAttributeId +
                ", amenityId=" + amenityId +
                ", value=" + value +
                '}';
    }
}

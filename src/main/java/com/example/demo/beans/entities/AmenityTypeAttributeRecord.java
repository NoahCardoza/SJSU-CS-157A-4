package com.example.demo.beans.entities;

public class AmenityTypeAttributeRecord {

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
                "amenityMetricId=" + amenityAttributeId +
                ", reviewId=" + amenityId +
                ", value=" + value +
                '}';
    }
}

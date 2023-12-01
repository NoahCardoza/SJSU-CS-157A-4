package com.example.demo.beans.entities;

public class AmenityTypeMetricRecordAveragesWithName {
    Long amenityTypeMetricId;
    Long reviewId;
    Float value;
    String name;

    public Long getAmenityTypeMetricId() {
        return amenityTypeMetricId;
    }

    public void setAmenityTypeMetricId(Long amenityTypeMetricId) {
        this.amenityTypeMetricId = amenityTypeMetricId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    @Override
    public String toString() {
        return "AmenityTypeMetricRecord{" +
                "amenityMetricId=" + amenityTypeMetricId +
                ", reviewId=" + reviewId +
                ", value=" + value +
                ", name=" + name +
                '}';
    }
}

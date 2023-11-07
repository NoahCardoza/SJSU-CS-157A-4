package com.example.demo.beans.entities;

public class AmenityTypeMetricRecord {
    Long amenityMetricId;
    Long reviewId;
    Integer value;

    public Long getAmenityMetricId() {
        return amenityMetricId;
    }

    public void setAmenityMetricId(Long amenityMetricId) {
        this.amenityMetricId = amenityMetricId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AmenityTypeMetricRecord{" +
                "amenityMetricId=" + amenityMetricId +
                ", reviewId=" + reviewId +
                ", value=" + value +
                '}';
    }
}

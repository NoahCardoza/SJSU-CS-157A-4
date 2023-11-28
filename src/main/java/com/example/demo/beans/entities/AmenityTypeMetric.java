package com.example.demo.beans.entities;

public class AmenityTypeMetric {
    Long id;
    Long amenityTypeId;
    String name;
    String type;

    public AmenityTypeMetric() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmenityTypeId() {
        return amenityTypeId;
    }

    public void setAmenityTypeId(Long amenityTypeId) {
        this.amenityTypeId = amenityTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AmenityTypeMetric{" +
                "id=" + id +
                ", amenityTypeId=" + amenityTypeId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

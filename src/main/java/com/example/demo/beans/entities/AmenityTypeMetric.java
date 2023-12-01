package com.example.demo.beans.entities;

public class AmenityTypeMetric {
    Long id;
    Long amenityTypeId;
    String name;
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
    @Override
    public String toString() {
        return "AmenityTypeMetric{" +
                "id=" + id +
                ", amenityTypeId=" + amenityTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}

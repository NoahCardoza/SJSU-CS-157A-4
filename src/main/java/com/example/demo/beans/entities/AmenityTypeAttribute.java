package com.example.demo.beans.entities;

public class AmenityTypeAttribute {
    private Long id;
    private Long amenityTypeId;
    private String name;
    private String type;

    // Constructors
    public AmenityTypeAttribute() {
        // Default constructor
    }

    public AmenityTypeAttribute(Long id, Long amenityTypeId, String name, String type) {
        this.id = id;
        this.amenityTypeId = amenityTypeId;
        this.name = name;
        this.type = type;
    }

    // Getters and Setters
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
        return "AmenityTypeAttribute{" +
                "id=" + id +
                ", amenityTypeId=" + amenityTypeId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
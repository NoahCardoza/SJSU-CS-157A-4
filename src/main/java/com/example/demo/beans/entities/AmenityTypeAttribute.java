package com.example.demo.beans.entities;

public class AmenityTypeAttribute {
    private Long id;
    private Long amenityTypeId;
    private String name;
    private String icon;
    private String type;

    // Constructors
    public AmenityTypeAttribute() {
        // Default constructor
    }

    public AmenityTypeAttribute(Long id, Long amenityTypeId, String name, String icon, String type) {
        this.id = id;
        this.amenityTypeId = amenityTypeId;
        this.name = name;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
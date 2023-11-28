package com.example.demo.beans.entities;

public class AmenityType {

    Long id;
    Long parentAmenityTypeId;
    String name;
    String description;

    public AmenityType() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentAmenityTypeId() {
        return parentAmenityTypeId;
    }

    public void setParentAmenityTypeId(Long parentAmenityTypeId) {
        this.parentAmenityTypeId = parentAmenityTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AmenityType{" +
                "id=" + id +
                ", parentAmenityTypeId=" + parentAmenityTypeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

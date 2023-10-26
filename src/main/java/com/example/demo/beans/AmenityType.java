package com.example.demo.beans;

public class AmenityType {

    Long id;
    Long parentAmenityTypeId;
    String name;
    String icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
                ", icon='" + icon + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

package com.example.demo.beans.entities;

import java.io.Serial;
import java.io.Serializable;

public class Amenity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long amenityTypeId;
    private Long locationId;
    private Long userId;
    private String description;
    private String name;
    private String createdAt;
    private String updatedAt;

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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Amenity{" +
                "id=" + id +
                ", amenityTypeId=" + amenityTypeId +
                ", locationId=" + locationId +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

package com.example.demo.beans.entities;

import java.io.Serial;
import java.io.Serializable;
import java.sql.*;
import java.util.List;

public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long parentLocationId;
    private Double longitude;
    private Double latitude;
    private String name;
    private String address;
    private String description;
    private String createdAt;
    private String updatedAt;

    // utility fields
    private List<Amenity> amenities;

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long
    getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Location{" +
                "id=" + id +
                ", userId=" + userId +
                ", parentLocationId=" + parentLocationId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

package com.example.demo.beans.entities;

public class UserStats {
    private Long id;
    private Integer reviews;
    private Integer amenities;
    private Integer locations;

    public UserStats() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public Integer getAmenities() {
        return amenities;
    }

    public void setAmenities(Integer amenities) {
        this.amenities = amenities;
    }

    public Integer getLocations() {
        return locations;
    }

    public void setLocations(Integer locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "id=" + id +
                ", reviews=" + reviews +
                ", amenities=" + amenities +
                ", locations=" + locations +
                '}';
    }
}

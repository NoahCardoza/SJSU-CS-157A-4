package com.example.demo.beans.entities;


import java.sql.Timestamp;

import java.util.List;


public class Review {
    private Long id;
    private Long amenityId;
    private Long userId;
    private String description;
    private String name;
    private Boolean hidden;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<String> images;
    private User user;

    private List<AmenityTypeMetricRecordWithName> metrics;

    private Integer voted;
    private Integer votes;

    public Review() {
        // Default constructor
    }

    public User getUser() {
        return user;
    }

    public float calculateAverageRating() {
        int sum = metrics.stream().mapToInt(AmenityTypeMetricRecord::getValue).sum();

        return (float) sum / metrics.size();
    }

    public void setUser(User user) {
        this.user = user;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Long amenityId) {
        this.amenityId = amenityId;
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

    public void setDescription(String description) {
        description = description.trim();
        description = description.replaceAll("(\\r\\n|\\r|\\n){2,}", "\n\n");

        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<AmenityTypeMetricRecordWithName> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<AmenityTypeMetricRecordWithName> metrics) {
        this.metrics = metrics;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public void setVoted(int voted) {
        this.votes = voted;
    }

    public int getVoted() {
        return votes;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", amenityId=" + amenityId +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", hidden=" + hidden +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
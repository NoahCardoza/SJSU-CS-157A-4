package com.example.demo.servlets.amenities;

import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.AmenityTypeMetric;
import com.example.demo.beans.entities.Review;
import com.example.demo.daos.AmenityTypeMetricDao;

import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

public class AmenitiesReviewGrouper {

    Long amenityId;
    Long amenityTypeId;
    List<Review> reviews;

    public AmenitiesReviewGrouper(Amenity amenity, List<Review> reviews){
        this.amenityId = amenity.getId();
        this.amenityTypeId = amenity.getAmenityTypeId();
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        if (reviews.isEmpty()) {
            return "";
        }

        String reviewString = "";
        StringJoiner joiner = new StringJoiner("\n", "", "\n");

        for(Review review : reviews){

            joiner.add(review.getName());

            if(review.getDescription() != null){
                joiner.add(review.getDescription());
            }

            joiner.add(review.getCreatedAt().toString());

        }

        reviewString = joiner.toString();

        return reviewString;
    }

}

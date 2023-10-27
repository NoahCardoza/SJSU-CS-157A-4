package com.example.demo.beans.entities;

public class AmenityWithImage extends Amenity {
    ReviewImage image;

    public AmenityWithImage() {
        super();
    }

    public ReviewImage getImage() {
        return image;
    }

    public void setImage(ReviewImage image) {
        this.image = image;
    }
}

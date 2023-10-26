package com.example.demo.beans;

public class AmenityWIthImage extends Amenity {
    ReviewImage image;

    public AmenityWIthImage() {
        super();
    }

    public ReviewImage getImage() {
        return image;
    }

    public void setImage(ReviewImage image) {
        this.image = image;
    }
}

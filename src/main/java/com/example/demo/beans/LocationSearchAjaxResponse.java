package com.example.demo.beans;

import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.Location;

import java.util.List;

public class LocationSearchAjaxResponse {
    private String html;

    private List<Location> locations;
//    private List<Amenity> amenities;

    public LocationSearchAjaxResponse(String html, List<Location> locations) {
        this.html = html;
        this.locations = locations;
//        this.amenities = amenities;
    }
//
//    public List<Amenity> getAmenities() {
//        return amenities;
//    }
//
//    public void setAmenities(List<Amenity> amenities) {
//        this.amenities = amenities;
//    }

    public String getHtml() {
        return html;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

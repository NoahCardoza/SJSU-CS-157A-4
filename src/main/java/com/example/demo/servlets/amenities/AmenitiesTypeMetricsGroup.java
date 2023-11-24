package com.example.demo.servlets.amenities;

import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.AmenityTypeMetric;
import com.example.demo.daos.AmenityTypeMetricDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class AmenitiesTypeMetricsGroup {

    Long amenityId;
    Long amenityTypeId;
    List<AmenityTypeMetric> metrics;

    public AmenitiesTypeMetricsGroup(Amenity amenity, List<AmenityTypeMetric> metrics){
        this.amenityId = amenity.getId();
        this.amenityTypeId = amenity.getAmenityTypeId();
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        if (metrics.isEmpty()) {
            return "";
        }

        String booleanAttributes = "";
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        try{
            //get the attribute id and then match it
            for(AmenityTypeMetric metric : metrics){
                //gets you the values from the database for the amenityType
                Long value = AmenityTypeMetricDao.getInstance().getAvgAmenityMetricValue(metric.getId());

                // may need to change, i'm unsure if there can be multiple values for one attribute
                if(value == 0){
                    joiner.add(metric.getName() + ": N/A");
                }
                else {
                    joiner.add(metric.getName() + ": " + value);
                }
            }

            booleanAttributes = joiner.toString();

            return booleanAttributes;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}

package com.example.demo.beans.entities;

public class AmenityTypeMetricRecordWithName extends AmenityTypeMetricRecord {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AmenityTypeMetricRecordWithName{" +
                super.toString() + "," +
                "name=" + name +
                '}';
    }
}

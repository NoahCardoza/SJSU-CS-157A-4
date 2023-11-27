package com.example.demo.beans.entities;

public class AmenityTypeAttributeRecordWithName extends AmenityTypeAttributeRecord{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AmenityTypeAttributeRecordWithName{" +
                super.toString() + "," +
                "name=" + name +
                '}';
    }
}

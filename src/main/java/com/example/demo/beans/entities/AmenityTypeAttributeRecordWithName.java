package com.example.demo.beans.entities;

public class AmenityTypeAttributeRecordWithName extends AmenityTypeAttributeRecord {
    private Long attributeId;;
    private String name;
    private String type;
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String generatePlaceholderText() {
        return switch (this.type) {
            case "number" -> "Enter a number";
            case "text" -> "Enter text";
            case "boolean" -> "Select yes or no";
            default -> "Enter a value";
        };
    }

    @Override
    public String toString() {
        return "AmenityTypeAttributeRecordWithName{" +
                super.toString() + "," +
                "name=" + name +
                '}';
    }
}

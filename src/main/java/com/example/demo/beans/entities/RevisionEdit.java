package com.example.demo.beans.entities;

public class RevisionEdit {
    private Long id;
    private Long revisionId;
    private String tableName;
    private Long primaryKey;
    private String columnName;
    private String previousValue;
    private String newValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Long revisionId) {
        this.revisionId = revisionId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return "RevisionEdit{" +
                "id=" + id +
                ", revisionId=" + revisionId +
                ", table='" + tableName + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", column='" + columnName + '\'' +
                ", previousValue='" + previousValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
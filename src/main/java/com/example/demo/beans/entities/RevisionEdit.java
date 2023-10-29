package com.example.demo.beans.entities;
import java.sql.Timestamp;

public class RevisionEdit {
    private Long id;
    private Long revisionId;
    private String table;
    private Long primaryKey;
    private String column;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
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
                ", table='" + table + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", column='" + column + '\'' +
                ", previousValue='" + previousValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
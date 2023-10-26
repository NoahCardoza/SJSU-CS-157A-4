package com.example.demo.daos;

import com.example.demo.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDao {
    Integer id;
    Integer userId;
    Integer parentLocationId;
    Double longitude;
    Double latitude;
    String name;
    String address;
    String description;
    String createdAt;
    String updatedAt;

    static public List<LocationDao> getAllLocations() throws SQLException {
        List<LocationDao> results = new ArrayList<>();
        Connection conn = Database.getConnection();

        if (conn == null) {
            return results;
        }

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Location");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            results.add(fromResultSet(resultSet));
        }

        return results;
    }

    static public LocationDao getLocationById(int id) throws SQLException {
        Connection conn = Database.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Location WHERE id = ?");
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return fromResultSet(resultSet);
        }

        return null;
    }

    static public List<LocationDao> getParentLocationsOf(Integer id) throws SQLException {
        List<LocationDao> results = new ArrayList<>();
        Connection conn = Database.getConnection();

        if (conn == null) {
            return results;
        }

        PreparedStatement statement;

        if (id == null) {
            statement = conn.prepareStatement("SELECT * FROM Location WHERE parent_location_id IS NULL");
        } else {
            statement = conn.prepareStatement("SELECT * FROM Location WHERE parent_location_id = ?");
            statement.setInt(1, id);
        }

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            results.add(fromResultSet(resultSet));
        }

        return results;
    }

    static private LocationDao fromResultSet(ResultSet row) throws SQLException {
        LocationDao entity = new LocationDao();

        entity.setId(row.getInt(1));
        entity.setUserId(row.getInt(2));

        entity.setParentLocationId(row.getInt(3));
        if (row.wasNull()) entity.setParentLocationId(null);

        entity.setLongitude(row.getDouble(4));
        entity.setLatitude(row.getDouble(5));
        entity.setName(row.getString(6));
        entity.setAddress(row.getString(7));
        entity.setDescription(row.getString(8));
        entity.setCreatedAt(row.getString(9));
        entity.setUpdatedAt(row.getString(10));

        return entity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Integer parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void create() {
        try {
            Connection conn = Database.getConnection();

            if (conn == null) {
                return;
            }

            PreparedStatement statement = conn.prepareStatement("INSERT INTO Location (user_id, parent_location_id, longitude, latitude, name, address, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, this.getUserId());
            if (this.getParentLocationId() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, this.getParentLocationId());
            }
            statement.setDouble(3, this.getLongitude());
            statement.setDouble(4, this.getLatitude());
            statement.setString(5, this.getName());
            statement.setString(6, this.getAddress());
            statement.setString(7, this.getDescription());

            statement.executeUpdate();

            // todo: get the id of the newly created location

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

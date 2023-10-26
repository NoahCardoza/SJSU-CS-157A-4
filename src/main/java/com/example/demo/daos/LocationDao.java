package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.Location;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDao implements Dao<Location> {
    static LocationDao instance = null;
    static public LocationDao getInstance() {
        if (instance == null) {
            instance = new LocationDao();
        }
        return instance;
    }

    private LocationDao() {}

    public List<Location> getAll() throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        List<Location> results = new ArrayList<>();

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

    public Optional<Location> get(long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Location WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }

    public List<Location> getParentLocationsOf(Long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        List<Location> results = new ArrayList<>();

        PreparedStatement statement;

        if (id == null) {
            statement = conn.prepareStatement("SELECT * FROM Location WHERE parent_location_id IS NULL");
        } else {
            statement = conn.prepareStatement("SELECT * FROM Location WHERE parent_location_id = ?");
            statement.setLong(1, id);
        }

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            results.add(fromResultSet(resultSet));
        }

        return results;
    }

    static private Location fromResultSet(ResultSet row) throws SQLException {
        Location location = new Location();

        location.setId(row.getLong(1));
        location.setUserId(row.getInt(2));

        location.setParentLocationId(row.getLong(3));
        if (row.wasNull()) location.setParentLocationId(null);

        location.setLongitude(row.getDouble(4));
        location.setLatitude(row.getDouble(5));
        location.setName(row.getString(6));
        location.setAddress(row.getString(7));
        location.setDescription(row.getString(8));
        location.setCreatedAt(row.getString(9));
        location.setUpdatedAt(row.getString(10));

        return location;
    }

    public void create(Location location) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("INSERT INTO Location (user_id, parent_location_id, longitude, latitude, name, address, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setInt(1, location.getUserId());
        if (location.getParentLocationId() == null) {
            statement.setNull(2, Types.INTEGER);
        } else {
            statement.setDouble(2, location.getParentLocationId());
        }
        statement.setDouble(3, location.getLongitude());
        statement.setDouble(4, location.getLatitude());
        statement.setString(5, location.getName());
        statement.setString(6, location.getAddress());
        statement.setString(7, location.getDescription());

        statement.executeUpdate();

        // todo: get the id of the newly created location
    }
}

package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class LocationDao {
    static LocationDao instance = null;
    static public LocationDao getInstance() {
        if (instance == null) {
            instance = new LocationDao();
        }
        return instance;
    }

    private LocationDao() {}

    public List<Location> getAll() throws SQLException {
        try (Connection conn = Database.getConnection()) {

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
    }

    public Optional<Location> get(long id) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Location WHERE id = ?")) {
                statement.setDouble(1, id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return Optional.of(fromResultSet(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public List<Location> getParentLocationsOf(Long id) throws SQLException {
        List<Location> results = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
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
        }

        return results;
    }

    static private Location fromResultSet(ResultSet row) throws SQLException {
        Location location = new Location();

        location.setId(row.getLong(1));
        location.setUserId(row.getLong(2));

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

    public Long create(Location location) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Location (user_id, parent_location_id, longitude, latitude, name, address, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, location.getUserId());
            if (location.getParentLocationId() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setDouble(2, location.getParentLocationId());
            }
            ps.setDouble(3, location.getLongitude());
            ps.setDouble(4, location.getLatitude());
            ps.setString(5, escapeHtml(location.getName()));
            ps.setString(6, escapeHtml(location.getAddress()));
            ps.setString(7, escapeHtml(location.getDescription()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            location.setId(rs.getLong(1));
            
            return location.getId();
        }
    }


    public void update(Location location) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE Location SET user_id = ?, parent_location_id = ?, longitude = ?, latitude = ?, name = ?, address = ?, description = ? WHERE id = ?");

            ps.setLong(1, location.getUserId());

            if (location.getParentLocationId() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setDouble(2, location.getParentLocationId());
            }
            ps.setDouble(3, location.getLongitude());
            ps.setDouble(4, location.getLatitude());
            ps.setString(5, escapeHtml(location.getName()));
            ps.setString(6, escapeHtml(location.getAddress()));
            ps.setString(7, escapeHtml(location.getDescription()));
            ps.setLong(8, location.getId());

            ps.executeUpdate();
        }
    }

    public ArrayList<Location> search(Double longitude, Double latitude, Integer radius) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Location WHERE ST_Distance_Sphere(point(longitude, latitude), point(?, ?)) <= ?");
            ps.setDouble(1, longitude);
            ps.setDouble(2, latitude);
            ps.setDouble(3, radius);

            ResultSet rs = ps.executeQuery();

            ArrayList<Location> locations = new ArrayList<>();

            while (rs.next()) {
                locations.add(fromResultSet(rs));
            }

            return locations;
        }
    }
}

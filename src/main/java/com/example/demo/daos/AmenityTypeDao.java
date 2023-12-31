package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.AmenityType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class AmenityTypeDao {
    public static final String DELETE_AMENITY_TYPE = "DELETE FROM AmenityType WHERE id = ?";
    public static final String UPDATE_AMENITY_TYPE = "UPDATE AmenityType SET name = ?, description = ?, parent_amenity_type_id = ? WHERE id = ?";
    public static final String SELECT_ALL_AMENITY_TYPES = "SELECT * FROM AmenityType";
    public static final String SELECT_AMENITY_TYPE_BY_ID = "SELECT * FROM AmenityType WHERE id = ?";
    static AmenityTypeDao instance = null;
    static public AmenityTypeDao getInstance() {
        if (instance == null) {
            instance = new AmenityTypeDao();
        }
        return instance;
    }

    private AmenityTypeDao() {}

    private AmenityType fromResultSet(ResultSet resultSet) throws SQLException {
        AmenityType amenityType = new AmenityType();

        amenityType.setId(resultSet.getLong("id"));
        amenityType.setName(resultSet.getString("name"));
        amenityType.setDescription(resultSet.getString("description"));
        amenityType.setParentAmenityTypeId(resultSet.getLong("parent_amenity_type_id"));

        return amenityType;
    }

    public Optional<AmenityType> get(long id) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(SELECT_AMENITY_TYPE_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(fromResultSet(resultSet));
            }
        }

        return Optional.empty();
    }

    public List<AmenityType> getAll() throws SQLException {
        ArrayList<AmenityType> amenityTypes = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(SELECT_ALL_AMENITY_TYPES);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                amenityTypes.add(fromResultSet(resultSet));
            }
        }

        return amenityTypes;
    }

    public Long create(AmenityType amenityType) throws SQLException {

        try (Connection conn = Database.getConnection()) {
            Long id = 0L;

            var ps = conn.prepareStatement("INSERT INTO AmenityType (name, description) VALUES (?, ?)");

            ps.setString(1, escapeHtml(amenityType.getName()));
            ps.setString(2, escapeHtml(amenityType.getDescription()));
            //ps.setLong(4, amenityType.getParentAmenityTypeId());
            ps.executeUpdate();


            var ret = conn.prepareStatement("SELECT id FROM AmenityType WHERE name = ?");
            ret.setString(1, escapeHtml(amenityType.getName()));
            ResultSet  rs = ret.executeQuery();

            while(rs.next()){
                id = rs.getLong("id");
            }

            return id;
        }
    }

    public void update(AmenityType amenityType) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_AMENITY_TYPE);
            statement.setString(1, escapeHtml(amenityType.getName()));
            statement.setString(2, escapeHtml(amenityType.getDescription()));
            statement.setLong(3, amenityType.getParentAmenityTypeId());
            statement.setLong(4, amenityType.getId());
            statement.executeUpdate();
        }
    }

    public void delete(Long amenityTypeId) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_AMENITY_TYPE);
            statement.setLong(1, amenityTypeId);
            statement.executeUpdate();
        }
    }

}

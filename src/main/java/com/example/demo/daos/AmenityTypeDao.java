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

public class AmenityTypeDao implements Dao<AmenityType> {
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
        amenityType.setIcon(resultSet.getString("icon"));
        amenityType.setDescription(resultSet.getString("description"));
        amenityType.setParentAmenityTypeId(resultSet.getLong("parent_amenity_type_id"));

        return amenityType;
    }

    @Override
    public Optional<AmenityType> get(long id) throws SQLException {
        var ps = Database.getInstance().getConnection().prepareStatement("SELECT * FROM AmenityType WHERE id = ?");
        ps.setLong(1, id);
        var rs = ps.executeQuery();

        if (rs.next()) {
            return Optional.of(fromResultSet(rs));
        }

        return Optional.empty();
    }

    @Override
    public List<AmenityType> getAll() throws SQLException {
        ArrayList<AmenityType> amenityTypes = new ArrayList<>();
        Connection conn = Database.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM AmenityType");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }

    @Override
    public Long create(AmenityType amenityType) throws SQLException {
        return null;
    }

    public void update(AmenityType amenityType) throws SQLException {
        var ps = Database.getInstance().getConnection().prepareStatement("UPDATE AmenityType SET name = ?, icon = ?, description = ?, parent_amenity_type_id = ? WHERE id = ?");
        ps.setString(1, amenityType.getName());
        ps.setString(2, amenityType.getIcon());
        ps.setString(3, amenityType.getDescription());
        ps.setLong(4, amenityType.getParentAmenityTypeId());
        ps.setLong(5, amenityType.getId());
        ps.executeUpdate();
    }

    public void delete(Long amenityTypeId) throws SQLException {
        var ps = Database.getInstance().getConnection().prepareStatement("DELETE FROM AmenityType WHERE id = ?");
        ps.setLong(1, amenityTypeId);
        ps.executeUpdate();
    }
}

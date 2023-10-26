package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.AmenityType;
import com.example.demo.beans.AmenityTypeAttribute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityTypeAttributeDao implements Dao<AmenityTypeAttribute> {
    static AmenityTypeAttributeDao instance = null;
    static public AmenityTypeAttributeDao getInstance() {
        if (instance == null) {
            instance = new AmenityTypeAttributeDao();
        }
        return instance;
    }

    private AmenityTypeAttributeDao() {}

    private AmenityTypeAttribute fromResultSet(ResultSet resultSet) throws SQLException {
        AmenityTypeAttribute amenityTypeAttribute = new AmenityTypeAttribute();

        amenityTypeAttribute.setId(resultSet.getLong("id"));
        amenityTypeAttribute.setName(resultSet.getString("name"));
        amenityTypeAttribute.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
        amenityTypeAttribute.setIcon(resultSet.getString("icon"));
        amenityTypeAttribute.setType(resultSet.getString("type"));

        return amenityTypeAttribute;
    }

    @Override
    public Optional get(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<AmenityTypeAttribute> getAll() throws SQLException {
        ArrayList<AmenityTypeAttribute> amenityTypes = new ArrayList<>();
        Connection conn = Database.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM AmenityType");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }

    @Override
    public Long create(AmenityTypeAttribute amenityTypeAttribute) throws SQLException {
        return null;
    }


    public List<AmenityTypeAttribute> getAllByAmenityType(Long amenityTypeId) throws SQLException {
        ArrayList<AmenityTypeAttribute> amenityTypes = new ArrayList<>();

        Connection conn = Database.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM AmenityTypeAttribute WHERE amenity_type_id = ?"
        );

        statement.setDouble(1, amenityTypeId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }
}

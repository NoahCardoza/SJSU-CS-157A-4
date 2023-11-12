package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.MinMax;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.beans.entities.AmenityTypeMetric;
import com.example.demo.beans.entities.AmenityTypeMetricRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityTypeMetricDao {
    static public String TYPE_NUMBER_ID_PREFIX = "amenityTypeNumberAttribute-";
    static public String TYPE_TEXT_ID_PREFIX = "amenityTypeTextAttribute-";
    static public String TYPE_BOOLEAN_ID = "amenityTypeBooleanAttributes";
    static AmenityTypeMetricDao instance = null;
    static public AmenityTypeMetricDao getInstance() {
        if (instance == null) {
            instance = new AmenityTypeMetricDao();
        }
        return instance;
    }

    private AmenityTypeMetricDao() {}

    private AmenityTypeMetric fromResultSet(ResultSet resultSet) throws SQLException {
        AmenityTypeMetric amenityTypeMetric = new AmenityTypeMetric();

        amenityTypeMetric.setId(resultSet.getLong("id"));
        amenityTypeMetric.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
        amenityTypeMetric.setName(resultSet.getString("name"));
        amenityTypeMetric.setIcon(resultSet.getString("icon"));
        amenityTypeMetric.setType(resultSet.getString("type"));

        return amenityTypeMetric;
    }


    public List<AmenityTypeMetric> getAllByAmenityType(Long amenityTypeId) throws SQLException {
        ArrayList<AmenityTypeMetric> amenityTypes = new ArrayList<>();

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM AmenityTypeMetric WHERE amenity_type_id = ?"
        );

        statement.setDouble(1, amenityTypeId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }


}

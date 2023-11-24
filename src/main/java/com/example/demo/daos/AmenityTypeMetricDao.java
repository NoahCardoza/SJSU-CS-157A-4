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

import static com.example.demo.Security.escapeHtml;

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

    public void create(AmenityTypeMetric amenityTypeMetric) throws SQLException {
        var ps = Database.getConnection().prepareStatement("INSERT INTO AmenityTypeMetric (amenity_type_id, name, icon, type) VALUES (?, ?, ?, ?)");

        ps.setLong(1, amenityTypeMetric.getAmenityTypeId());
        ps.setString(2, escapeHtml(amenityTypeMetric.getName()));
        ps.setString(3, escapeHtml(amenityTypeMetric.getIcon()));
        ps.setString(4, escapeHtml(amenityTypeMetric.getType()));
        ps.executeUpdate();
    }

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

    public Long getAvgAmenityMetricValue(Long metricId) throws SQLException {
        long avgMetricValue;

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT AVG(value) AS value FROM ReviewMetricRecord WHERE amenity_metric_id = ?"
        );

        statement.setDouble(1, metricId);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            avgMetricValue = resultSet.getLong("value");
        }
        else {
            avgMetricValue = 0;
        }

        return avgMetricValue;
    }

}

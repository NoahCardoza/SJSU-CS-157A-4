package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

        try (Connection conn = Database.getConnection()) {
            var ps = conn.prepareStatement("INSERT INTO AmenityTypeMetric (amenity_type_id, name) VALUES (?, ?)");

            ps.setLong(1, amenityTypeMetric.getAmenityTypeId());
            ps.setString(2, escapeHtml(amenityTypeMetric.getName()));
            ps.executeUpdate();
        }
    }

    private AmenityTypeMetric fromResultSet(ResultSet resultSet) throws SQLException {
        AmenityTypeMetric amenityTypeMetric = new AmenityTypeMetric();

        amenityTypeMetric.setId(resultSet.getLong("id"));
        amenityTypeMetric.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
        amenityTypeMetric.setName(resultSet.getString("name"));

        return amenityTypeMetric;
    }


    public List<AmenityTypeMetric> getAllByAmenityType(Long amenityTypeId) throws SQLException {
        ArrayList<AmenityTypeMetric> amenityTypes = new ArrayList<>();

        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM AmenityTypeMetric WHERE amenity_type_id IN (" +
                        "WITH RECURSIVE amenity_type_hierarchy AS (\n" +
                        "        SELECT id, parent_amenity_type_id\n" +
                        "        FROM AmenityType\n" +
                        "        WHERE id = ?\n" +
                        "        UNION ALL\n" +
                        "        SELECT\n" +
                        "        e.id,\n" +
                        "        e.parent_amenity_type_id\n" +
                        "        FROM AmenityType e, amenity_type_hierarchy\n" +
                        "        WHERE amenity_type_hierarchy.parent_amenity_type_id = e.id\n" +
                        "    ) SELECT id FROM amenity_type_hierarchy)"
        );

        statement.setDouble(1, amenityTypeId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }

    public List<AmenityTypeMetricRecordAveragesWithName> getAmenityMetricAverages(Amenity amenity) throws SQLException {
        ArrayList<AmenityTypeMetricRecordAveragesWithName> amenityTypes = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("""
            SELECT amenity_type_id, name, AVG(value) AS value
            FROM (
                    SELECT * FROM AmenityTypeMetric WHERE amenity_type_id IN (
                    WITH RECURSIVE amenity_type_hierarchy AS (
                        SELECT id, parent_amenity_type_id
                        FROM AmenityType
                        WHERE id = ?
                        UNION ALL
                        SELECT
                        e.id,
                        e.parent_amenity_type_id
                        FROM AmenityType e, amenity_type_hierarchy
                        WHERE amenity_type_hierarchy.parent_amenity_type_id = e.id
                    ) SELECT id FROM amenity_type_hierarchy
                )
            ) A
            LEFT JOIN (
                SELECT * FROM ReviewMetricRecord WHERE review_id IN (SELECT id FROM Review WHERE amenity_id=? AND hidden=0)
            ) B
            ON A.id=B.amenity_metric_id
            GROUP BY amenity_type_id, name
        """);

            statement.setDouble(1, amenity.getAmenityTypeId());
            statement.setDouble(2, amenity.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                AmenityTypeMetricRecordAveragesWithName amenityTypeMetric = new AmenityTypeMetricRecordAveragesWithName();

                amenityTypeMetric.setAmenityTypeMetricId(resultSet.getLong("amenity_type_id"));
                amenityTypeMetric.setName(resultSet.getString("name"));
                amenityTypeMetric.setValue(resultSet.getFloat("value"));
                amenityTypes.add(amenityTypeMetric);
            }
            return amenityTypes;
        }
}

}

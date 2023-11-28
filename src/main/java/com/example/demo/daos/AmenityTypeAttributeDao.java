package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.MinMax;
import com.example.demo.beans.entities.AmenityTypeAttribute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class AmenityTypeAttributeDao {
    static public String TYPE_NUMBER_ID_PREFIX = "amenityTypeNumberAttribute-";
    static public String TYPE_TEXT_ID_PREFIX = "amenityTypeTextAttribute-";
    static public String TYPE_BOOLEAN_ID = "amenityTypeBooleanAttributes";
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
        amenityTypeAttribute.setType(resultSet.getString("type"));

        return amenityTypeAttribute;
    }

    public Optional get(long id) throws SQLException {
        return Optional.empty();
    }

    public List<AmenityTypeAttribute> getAll() throws SQLException {
        ArrayList<AmenityTypeAttribute> amenityTypes = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM AmenityType");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }

    public void create(AmenityTypeAttribute amenityTypeAttribute) throws SQLException {
        var ps = Database.getConnection().prepareStatement("INSERT INTO AmenityTypeAttribute (amenity_type_id, name, type) VALUES (?, ?, ?)");

        ps.setLong(1, amenityTypeAttribute.getAmenityTypeId());
        ps.setString(2, escapeHtml(amenityTypeAttribute.getName()));
        ps.setString(3, escapeHtml(amenityTypeAttribute.getType()));
        ps.executeUpdate();
    }

    public List<AmenityTypeAttribute> getAllByAmenityType(Long amenityTypeId) throws SQLException {
        ArrayList<AmenityTypeAttribute> amenityTypes = new ArrayList<>();

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM AmenityTypeAttribute WHERE amenity_type_id IN (" +
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

    public List<String> getAllTextValuesForAttribute(Long attributeId) throws SQLException {
        ArrayList<String> attributeValues = new ArrayList<>();

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT DISTINCT value FROM AmenityAttributeRecord WHERE amenity_attribute_id = ?"
        );

        statement.setDouble(1, attributeId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            attributeValues.add(resultSet.getString("value"));
        }

        return attributeValues;
    }

    public String getAllValuesForAttribute(Long attributeId, Long amenityId) throws SQLException {
        String attributeValue = "";

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT DISTINCT value FROM AmenityAttributeRecord WHERE amenity_attribute_id = ? AND amenity_id = ?"
        );

        statement.setDouble(1, attributeId);
        statement.setDouble(2, amenityId);


        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            attributeValue += resultSet.getString("value");
        }

        return attributeValue;
    }

    public String getValueForAttribute(Long attributeId, Long amenityId) throws SQLException {
        String attributeValue = "";

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT DISTINCT value FROM AmenityAttributeRecord WHERE amenity_attribute_id = ? AND amenity_id = ?"
        );

        statement.setDouble(1, attributeId);
        statement.setDouble(2, amenityId);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            attributeValue = resultSet.getString("value");
        }
        return attributeValue;
    }

    public String getAttributeName(Long attributeId) throws SQLException {

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT name FROM AmenityTypeAttribute WHERE id = ?"
        );

        statement.setLong(1, attributeId);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("name");
        }

        return "";

    }

    public Optional<MinMax> getMinMaxIntValuesForAttribute(Long attributeId) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT MIN(value), MAX(value) FROM AmenityAttributeRecord WHERE amenity_attribute_id = ?"
        );

        statement.setDouble(1, attributeId);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            try {
                MinMax minMax = new MinMax(
                        resultSet.getInt(1),
                        resultSet.getInt(2)
                );
                return Optional.of(minMax);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }
}

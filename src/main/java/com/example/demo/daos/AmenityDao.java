package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.Amenity;
import com.example.demo.beans.entities.AmenityWithImage;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.ReviewImage;
import com.example.demo.servlets.search.AmenityFilter;

import java.sql.*;
import java.util.*;

public class AmenityDao implements Dao<Amenity> {
    static AmenityDao instance = null;
    static public AmenityDao getInstance() {
        if (instance == null) {
            instance = new AmenityDao();
        }
        return instance;
    }

    private AmenityDao() {}

    private Amenity fromResultSet(ResultSet resultSet) throws SQLException {
        Amenity amenity = new Amenity();

        amenity.setId(resultSet.getLong("id"));
        amenity.setName(resultSet.getString("name"));
        amenity.setDescription(resultSet.getString("description"));
        amenity.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
        amenity.setLocationId(resultSet.getLong("location_id"));
        amenity.setUserId(resultSet.getLong("user_id"));
        amenity.setAccessible(resultSet.getBoolean("accessible"));
        amenity.setCreatedAt(resultSet.getString("created_at"));
        amenity.setUpdatedAt(resultSet.getString("updated_at"));

        return amenity;
    }

    @Override
    public Optional<Amenity> get(long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Amenity WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }


    public void delete(long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("DELETE FROM Amenity WHERE id = ?");
        statement.setDouble(1, id);
    }

    @Override
    public List<Amenity> getAll() throws SQLException {
        ArrayList<Amenity> amenityTypes = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Amenity");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            amenityTypes.add(fromResultSet(resultSet));
        }

        return amenityTypes;
    }



    @Override
    public Long create(Amenity amenityType) throws SQLException {
        return null;
    }

    public List<AmenityWithImage> getWithFilter(AmenityFilter filter) throws SQLException {
        ArrayList<AmenityWithImage> amenityTypes = new ArrayList<>();
        Connection conn = Database.getConnection();

        PreparedStatement stmt ;
        ResultSet resultSet;
        ArrayList<String> params = new ArrayList<>();
        String filterSubQuery = "";
        String statement = "SELECT *, (SELECT url FROM ReviewImage WHERE review_id IN (SELECT id FROM Review WHERE amenity_id = Amenity.id ORDER BY id) ORDER BY id LIMIT 1) AS image FROM Amenity\n";
        if (filter.getAmenityTypeId() != null) {
            statement = statement +
                    "WHERE\n" +
                    "    Amenity.amenity_type_id IN (\n" +
                    "    WITH RECURSIVE amenity_type_hierarchy AS (\n" +
                    "        SELECT    id,\n" +
                    "                parent_amenity_type_id\n" +
                    "        FROM AmenityType\n" +
                    "        WHERE id = ?\n" +
                    "\n" +
                    "        UNION ALL\n" +
                    "\n" +
                    "        SELECT\n" +
                    "        e.id,\n" +
                    "        e.parent_amenity_type_id\n" +
                    "        FROM AmenityType e, amenity_type_hierarchy\n" +
                    "        WHERE amenity_type_hierarchy.id = e.parent_amenity_type_id\n" +
                    "    ) SELECT id FROM amenity_type_hierarchy\n" +
                    ")";
            params.add(filter.getAmenityTypeId().toString());
        }

        if (!filter.getBooleanAttributes().isEmpty() || !filter.getNumberAttributes().isEmpty() || !filter.getTextAttributes().isEmpty()) {
            StringJoiner joiner = new StringJoiner(",");
            StringJoiner valueJoiner = new StringJoiner(",");
            ArrayList<String> valueParams = new ArrayList<>();
            ArrayList<String> idParams = new ArrayList<>();

            filterSubQuery = "SELECT DISTINCT amenity_id FROM AmenityAttributeRecord WHERE value IN (";

            if (!filter.getBooleanAttributes().isEmpty()) {
                valueJoiner.add("'T'"); // find all the true values
                for (String attributeId : filter.getBooleanAttributes()) {
                    joiner.add("?");
                    idParams.add(attributeId);
                }
            }
            filter.getTextAttributes().forEach((attributeId, values) -> {
                // TODO: later we might move to OR instead of AND logic
                // right now there should only every be one value per attribute
                for (String value : values) {
                    valueJoiner.add("?");
                    joiner.add("?");
                    valueParams.add(value);
                    idParams.add(attributeId);
                }
            });

            filter.getNumberAttributes().forEach((attributeId, values) -> {
                // TODO: later we might move to min/max instead of exact value
                // right now there should only every be one value per attribute
                for (String value : values) {
                    valueJoiner.add("?");
                    joiner.add("?");
                    valueParams.add(value);
                    idParams.add(attributeId);
                }
            });

            filterSubQuery = filterSubQuery + valueJoiner + ") AND amenity_attribute_id IN (" + joiner + ") GROUP BY amenity_id HAVING COUNT(*) >= ?\n";

            params.addAll(valueParams);
            params.addAll(idParams);
            params.add(String.valueOf(filter.getBooleanAttributes().size() + filter.getNumberAttributes().size() + filter.getTextAttributes().size()));
        }

        if (!filterSubQuery.isEmpty()) {
            statement = statement + "AND Amenity.id IN (" + filterSubQuery + ")\n";
        }

        stmt = conn.prepareStatement(statement);
        for (int i = 0; i < params.size(); i++) {
            stmt.setString(i+1, params.get(i));
        }
        System.out.println(stmt.toString());
        resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            AmenityWithImage amenity = new AmenityWithImage();

            amenity.setId(resultSet.getLong("id"));
            amenity.setName(resultSet.getString("name"));
            amenity.setDescription(resultSet.getString("description"));
            amenity.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
            amenity.setLocationId(resultSet.getLong("location_id"));
            amenity.setUserId(resultSet.getLong("user_id"));
            amenity.setAccessible(resultSet.getBoolean("accessible"));
            amenity.setCreatedAt(resultSet.getString("created_at"));
            amenity.setUpdatedAt(resultSet.getString("updated_at"));
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setUrl(resultSet.getString("image"));
            amenity.setImage(reviewImage);
            amenityTypes.add(amenity);
        }

        return amenityTypes;
    }

    public List<AmenityWithImage> getFromLocationId(Long locationId) throws SQLException {
        ArrayList<AmenityWithImage> amenityTypes = new ArrayList<>();
        Connection conn = Database.getConnection();

        PreparedStatement statement;
        ResultSet resultSet;

        statement = conn.prepareStatement("SELECT\n" +
                "    *,\n" +
                "    (SELECT url FROM ReviewImage WHERE review_id IN (SELECT id FROM Review WHERE amenity_id = Amenity.id ORDER BY id) ORDER BY id LIMIT 1) AS image\n" +
                "FROM Amenity\n" +
                "WHERE\n" +
                "    Amenity.location_id IN (\n" +
                "    WITH RECURSIVE location_hierarchy AS (\n" +
                "        SELECT  id,\n" +
                "                parent_location_id\n" +
                "        FROM Location\n" +
                "        WHERE id = ?\n" +
                "\n" +
                "        UNION ALL\n" +
                "\n" +
                "        SELECT\n" +
                "        e.id,\n" +
                "        e.parent_location_id\n" +
                "        FROM Location e, location_hierarchy\n" +
                "        WHERE location_hierarchy.id = e.parent_location_id\n" +
                "    ) SELECT id FROM location_hierarchy\n" +
                ");");
        statement.setLong(1, locationId);

        System.out.println(statement);

        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            AmenityWithImage amenity = new AmenityWithImage();

            amenity.setId(resultSet.getLong("id"));
            amenity.setName(resultSet.getString("name"));
            amenity.setDescription(resultSet.getString("description"));
            amenity.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
            amenity.setLocationId(resultSet.getLong("location_id"));
            amenity.setUserId(resultSet.getLong("user_id"));
            amenity.setAccessible(resultSet.getBoolean("accessible"));
            amenity.setCreatedAt(resultSet.getString("created_at"));
            amenity.setUpdatedAt(resultSet.getString("updated_at"));
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setUrl(resultSet.getString("image"));
            amenity.setImage(reviewImage);
            amenityTypes.add(amenity);
        }

        System.out.println(amenityTypes);

        return amenityTypes;
    }

    public List<Amenity> getAmenityTypeId(Long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        List<Amenity> results = new ArrayList<>();

        PreparedStatement statement;

        if (id == null) {
            statement = conn.prepareStatement("SELECT * FROM Amenity WHERE amenity_type_id IS NULL");
        } else {
            statement = conn.prepareStatement("SELECT * FROM Amenity WHERE amenity_type_id = ?");
            statement.setLong(1, id);
        }

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            results.add(fromResultSet(resultSet));
        }

        return results;
    }
}

package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.*;
import com.example.demo.servlets.search.AmenityFilter;
import static com.example.demo.Security.escapeHtml;

import java.sql.*;
import java.util.*;

public class AmenityDao {
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
        amenity.setCreatedAt(resultSet.getString("created_at"));
        amenity.setUpdatedAt(resultSet.getString("updated_at"));

        return amenity;
    }

    public Optional<Amenity> get(long id) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Amenity WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }


    public void delete(long id) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("DELETE FROM Amenity WHERE id = ?");
        statement.setDouble(1, id);

        statement.executeQuery();
    }

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

    public void create(Amenity amenity) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("INSERT INTO Amenity (amenity_type_id, location_id, user_id, description, name) \n" +
                "VALUES (?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);

        statement.setLong(1, amenity.getAmenityTypeId());
        statement.setLong(2, amenity.getLocationId());
        statement.setLong(3, amenity.getUserId());
        statement.setString(4, escapeHtml(amenity.getDescription()));
        statement.setString(5, escapeHtml(amenity.getName()));

        statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();

        rs.next();

        amenity.setId(rs.getLong(1));

    }

    public void createAmenityRecord(AmenityTypeAttributeRecord record) {
        PreparedStatement ps = null;
        try {
            ps = Database.getConnection().prepareStatement(
                    "INSERT INTO AmenityAttributeRecord (amenity_attribute_id, amenity_id, value) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, record.getAmenityAttributeId());
            ps.setLong(2, record.getAmenityId());
            ps.setString(3, escapeHtml(record.getValue()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: need to update this statement with amenity
    public void update(Amenity amenity) throws SQLException {

        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("UPDATE hidden_gems.Amenity SET user_id = ?, description=?, name=? WHERE id = ?");

        statement.setLong(1, amenity.getUserId());
        statement.setString(2, escapeHtml(amenity.getDescription()));
        statement.setString(3, escapeHtml(amenity.getName()));
        statement.setLong(4, amenity.getId());

        statement.executeUpdate();
    }

    public void updateAmenityRecord(AmenityTypeAttributeRecord record) throws SQLException {

        try (Connection conn = Database.getConnection()) {
            PreparedStatement initialStatement = conn.prepareStatement(
                "SELECT * FROM AmenityAttributeRecord WHERE amenity_attribute_id = ? AND amenity_id = ?"
            );

            initialStatement.setLong(1, record.getAmenityAttributeId());
            initialStatement.setLong(2, record.getAmenityId());

            ResultSet rs = initialStatement.executeQuery();

            if(!rs.next()){
                createAmenityRecord(record);
            }
            else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE hidden_gems.AmenityAttributeRecord SET value = ? WHERE amenity_attribute_id = ? AND amenity_id = ?"
                );

                ps.setString(1, escapeHtml(record.getValue()));
                ps.setLong(2, record.getAmenityAttributeId());
                ps.setLong(3, record.getAmenityId());

                ps.executeUpdate();
            }

        }


    }

    public List<AmenityWithImage> getWithFilter(AmenityFilter filter, List<Location> locations) throws SQLException {
        ArrayList<AmenityWithImage> amenityTypes = new ArrayList<>();
        Connection conn = Database.getConnection();

        PreparedStatement stmt ;
        ResultSet resultSet;
        ArrayList<String> params = new ArrayList<>();
        String filterSubQuery = "";
        String statement = "SELECT *, (SELECT url FROM ReviewImage WHERE review_id IN (SELECT id FROM Review WHERE amenity_id = Amenity.id ORDER BY id) ORDER BY id LIMIT 1) AS image FROM Amenity\n";
        boolean whereAdded = false;
        if (locations != null) {
            StringJoiner locationIds = new StringJoiner(",");
            for (Location location : locations) {
                locationIds.add(location.getId().toString());
            }
            statement = statement + "WHERE location_id IN (" + locationIds + ")\n";
            whereAdded = true;
        }
        if (filter.getAmenityTypeId() != null) {
            if (!whereAdded) {
                statement = statement + "WHERE\n";
                whereAdded = true;
            } else {
                statement = statement + "AND\n";
            }
            statement = statement +
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
        resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            AmenityWithImage amenity = new AmenityWithImage();

            amenity.setId(resultSet.getLong("id"));
            amenity.setName(resultSet.getString("name"));
            amenity.setDescription(resultSet.getString("description"));
            amenity.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
            amenity.setLocationId(resultSet.getLong("location_id"));
            amenity.setUserId(resultSet.getLong("user_id"));
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

        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            AmenityWithImage amenity = new AmenityWithImage();

            amenity.setId(resultSet.getLong("id"));
            amenity.setName(resultSet.getString("name"));
            amenity.setDescription(resultSet.getString("description"));
            amenity.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
            amenity.setLocationId(resultSet.getLong("location_id"));
            amenity.setUserId(resultSet.getLong("user_id"));
            amenity.setCreatedAt(resultSet.getString("created_at"));
            amenity.setUpdatedAt(resultSet.getString("updated_at"));
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setUrl(resultSet.getString("image"));
            amenity.setImage(reviewImage);
            amenityTypes.add(amenity);
        }

        return amenityTypes;
    }


}

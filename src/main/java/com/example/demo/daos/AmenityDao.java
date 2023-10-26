package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.Amenity;
import com.example.demo.beans.AmenityWIthImage;
import com.example.demo.beans.ReviewImage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional get(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Amenity> getAll() throws SQLException {
        ArrayList<Amenity> amenityTypes = new ArrayList<>();
        Connection conn = Database.getInstance().getConnection();
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

    public List<Amenity> getWithFilter(Long amenityTypeId) throws SQLException {
        ArrayList<Amenity> amenityTypes = new ArrayList<>();
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement;
        ResultSet resultSet;

        if (amenityTypeId == null) {
            statement = conn.prepareStatement("SELECT * FROM Amenity");
        } else {
            statement = conn.prepareStatement("SELECT " +
                    "*, (SELECT url FROM ReviewImage WHERE review_id IN (SELECT id FROM Review WHERE amenity_id = Amenity.id ORDER BY id) ORDER BY id LIMIT 1) AS image\n" +
                    "FROM Amenity\n" +
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
                    ")");
            statement.setDouble(1, amenityTypeId);

        }
        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            AmenityWIthImage amenity = new AmenityWIthImage();

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
}

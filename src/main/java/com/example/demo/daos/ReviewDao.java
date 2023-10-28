package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.Review;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ReviewDao implements Dao<Review> {

    static ReviewDao instance = null;
    static public ReviewDao getInstance() {
        if (instance == null) {
            instance = new ReviewDao();
        }
        return instance;
    }

    private ReviewDao() {}


    public Optional<Review> get(long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Review WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }

    @Override
    public List<Review> getAll() throws SQLException {
        return null;
    }


    static private Review fromResultSet(ResultSet row) throws SQLException {
        Review review = new Review();

        review.setId(row.getLong(1));
        review.setAmenityId(row.getLong(2));
        review.setUserId(row.getLong(3));
        review.setDescription(row.getString(4));
        review.setName(row.getString(5));
        review.setHidden(row.getBoolean(6));
        review.setCreatedAt(row.getString(7));
        review.setUpdatedAt(row.getString(8));

        return review;
    }

    public Long create(Review review) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("INSERT INTO Review (user_id, parent_location_id, longitude, latitude, name, address, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setLong(1, review.getId());
        statement.setLong(2, review.getAmenityId());
        statement.setLong(3, review.getUserId());
        statement.setString(4, review.getDescription());
        statement.setString(5, review.getName());
        statement.setBoolean(6, review.getHidden());
        statement.setString(7, review.getCreatedAt());
        statement.setString(8, review.getUpdatedAt());

        statement.executeUpdate();

        review.setId(Database.getInstance().getLastInsertedId("Location"));

        return review.getId();

        // todo: get the id of the newly created location
    }
}

package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.Review;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ReviewDao {

    static ReviewDao instance = null;
    static public ReviewDao getInstance() {
        if (instance == null) {
            instance = new ReviewDao();
        }
        return instance;
    }

    private ReviewDao() {}


    public Optional<Review> get(long id) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Review WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }

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

    public static Long create(Review review) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("INSERT INTO Review (amenity_id,user_id, description,name) VALUES (?, ?, ?, ?)");

        statement.setLong(1, review.getAmenityId());
        statement.setLong(2, review.getUserId());
        statement.setString(3, review.getDescription());
        statement.setString(4, review.getName());

        statement.executeUpdate();

        review.setId(Database.getLastInsertedId("Review"));

        return review.getId();

        // todo: get the id of the newly created location
    }
}

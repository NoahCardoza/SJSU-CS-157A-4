package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.AmenityTypeMetric;
import com.example.demo.beans.entities.AmenityTypeMetricRecord;
import com.example.demo.beans.entities.AmenityTypeMetricRecordWithName;
import com.example.demo.beans.entities.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class ReviewDao {

    static ReviewDao instance = null;
    static public ReviewDao getInstance() {
        if (instance == null) {
            instance = new ReviewDao();
        }
        return instance;
    }

    private ReviewDao() {}


    public static Optional<Review> get(long id) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Review WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }
    public static Optional<Review> delete(long id) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("DELETE * FROM Review WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }

        return Optional.empty();
    }
    public static List<AmenityTypeMetricRecordWithName> getAllReviewMetricRecordsWithNames(long reviewId) throws SQLException {
        ArrayList<AmenityTypeMetricRecordWithName> records = new ArrayList<>();
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement(
                "SELECT name, amenity_metric_id, review_id, value FROM AmenityTypeMetric JOIN (SELECT * FROM ReviewMetricRecord WHERE review_id = ?) MetricRecord ON AmenityTypeMetric.id = MetricRecord.amenity_metric_id"
        );

        statement.setDouble(1, reviewId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            AmenityTypeMetricRecordWithName record = new AmenityTypeMetricRecordWithName();
            record.setName(resultSet.getString("name"));
            record.setAmenityMetricId(resultSet.getLong("amenity_metric_id"));
            record.setReviewId(resultSet.getLong("review_id"));
            record.setValue(resultSet.getInt("value"));
            records.add(record);
        }

        return records;
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
        statement.setString(3, escapeHtml(review.getDescription()));
        statement.setString(4, escapeHtml(review.getName()));

        statement.executeUpdate();

        review.setId(Database.getLastInsertedId("Review"));

        return review.getId();
    }

    public static void createReviewRecord(AmenityTypeMetricRecord record) throws SQLException {
        PreparedStatement ps = Database.getConnection().prepareStatement(
                "INSERT INTO ReviewMetricRecord (amenity_metric_id, review_id, value) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );

        ps.setLong(1, record.getAmenityMetricId());
        ps.setLong(2, record.getReviewId());
        ps.setDouble(3, record.getValue());

        ps.executeUpdate();
    }

    public List<Review> getAllReviews(Long amenityId) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM Review WHERE amenity_id = ?"
        );

        statement.setDouble(1, amenityId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            reviews.add(fromResultSet(resultSet));
        }

        return reviews;
    }
}

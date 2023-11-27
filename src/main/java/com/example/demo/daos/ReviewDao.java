package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.*;

import javax.xml.crypto.Data;
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

    public static void createReviewImage(Long id, String imageUrl) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ReviewImage (review_id, url) VALUES (?, ?)"
            );

            ps.setDouble(1, id);
            ps.setString(2, imageUrl);

            ps.executeUpdate();
        }
    }




    static private Review fromResultSet(ResultSet row) throws SQLException {
        Review review = new Review();

        review.setId(row.getLong(1));
        review.setAmenityId(row.getLong(2));
        review.setUserId(row.getLong(3));
        review.setDescription(row.getString(4));
        review.setName(row.getString(5));
        review.setHidden(row.getBoolean(6));
        review.setCreatedAt(row.getTimestamp(7));
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

    public static void delete(Review review) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("DELETE FROM Review WHERE id = ?");

        statement.setLong(1, review.getId());

        statement.executeUpdate();
    }

    public static void hide(Review review) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("UPDATE Review SET hidden = ? WHERE id = ?");

        statement.setBoolean(1, true); // set the hidden flag to true
        statement.setLong(2, review.getId());

        statement.executeUpdate();
    }

    public ArrayList<Review> getEntityReview(String tableName, Long primaryKey, Long currentUser) throws SQLException {
        ArrayList<Review> revisions = new ArrayList<>();
        Connection conn = Database.getConnection();
        // TODO: flag revisions that have been voted on by the current user
        PreparedStatement statement = conn.prepareStatement(
                "SELECT *, COALESCE((" +
                        "SELECT SUM(value) " +
                        "FROM ReviewVote " +
                        "WHERE review_id = Review.id), 0) AS votes " +
                        (currentUser != null
                                ? ", COALESCE((SELECT review_id FROM ReviewVote WHERE user_id = ? AND review_id = Review.id), 0) AS voted "
                                : " "
                        ) +
                        "FROM Review " +
                        "WHERE table_name = ? AND primary_key = ?"
        );
        int paramIndex = 1;
        if (currentUser != null) {
            statement.setLong(paramIndex++, currentUser);

        }
        statement.setString(paramIndex++, tableName);
        statement.setLong(paramIndex++, primaryKey);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Review review = fromResultSet(resultSet);
            review.setEdits(ReviewDao.getInstance().getAllByReviewId(review.getId()));
            review.setVotes(resultSet.getInt("votes"));
            if (currentUser != null) {
                review.setVoted(resultSet.getInt("voted"));
            }
            review.add(review);
        }

        return review;
    }

    public static void edit(Review review) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("UPDATE Review SET description=?, name=? WHERE id = ?");
        statement.setString(1, escapeHtml(review.getDescription()));
        statement.setString(2, escapeHtml(review.getName()));
        statement.setLong(3, review.getId());

        statement.executeUpdate();



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


    public static List<Review> getAllReviews(Long amenityId) throws SQLException {
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

    public static List<String> getAllImages(Long amenityId) throws SQLException {
        ArrayList<String> urls = new ArrayList<>();

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT url FROM ReviewImage x LEFT JOIN Review y ON x.review_id = y.id WHERE amenity_id = ?"
        );

        statement.setDouble(1, amenityId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            urls.add(resultSet.getString("url"));
        }

        return urls;

    }
}

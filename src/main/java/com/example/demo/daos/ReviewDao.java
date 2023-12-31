package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.S3;
import com.example.demo.beans.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class ReviewDao {

    public static final String SELECT_ALL_IMAGE_URLS_FROM_REVIEW = "SELECT url FROM ReviewImage WHERE review_id = ?";
    public static final String DELETE_REVIEW = "DELETE FROM Review WHERE id = ?";
    public static final String S3_REVIEW_IMAGE_PREFIX = "review-image-";
    static ReviewDao instance = null;
    static public ReviewDao getInstance() {
        if (instance == null) {
            instance = new ReviewDao();
        }
        return instance;
    }

    private ReviewDao() {}

    public static Optional<Review> get(long id) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Review WHERE id = ?"); //display all reviews based on the userID
            statement.setDouble(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(fromResultSet(resultSet));
            }

            return Optional.empty();
        }
    }
    
    public static List<AmenityTypeMetricRecordWithName> getAllReviewMetricRecordsWithNames(long reviewId) throws SQLException {
        ArrayList<AmenityTypeMetricRecordWithName> records = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {

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
        review.setUpdatedAt(row.getTimestamp(8));

        return review;
    }

    public static Long create(Review review) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Review (amenity_id,user_id, description,name) VALUES (?, ?, ?, ?)", //insert review description and name based on the userID and amenityID selected
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, review.getAmenityId());
            ps.setLong(2, review.getUserId());
            ps.setString(3, escapeHtml(review.getDescription()));
            ps.setString(4, escapeHtml(review.getName()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            review.setId(rs.getLong(1));

            return review.getId();
        }
    }

    public static void delete(Review review) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(SELECT_ALL_IMAGE_URLS_FROM_REVIEW);
            statement.setLong(1, review.getId());

            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> urls = new ArrayList<>();
            while (resultSet.next()) {
                urls.add(resultSet.getString("url"));
            }

            S3.deleteFiles(urls.stream().filter(url -> url.contains("/" + S3_REVIEW_IMAGE_PREFIX)).toList());

            statement = conn.prepareStatement(DELETE_REVIEW);
            statement.setLong(1, review.getId());
            statement.executeUpdate();
        }
    }

    public static void toggleHide(Review review) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement statement = conn.prepareStatement("UPDATE Review SET hidden = NOT hidden WHERE id = ?"); //set hidden to NOT hidden(1) when selected

            statement.setLong(1, review.getId());
            statement.executeUpdate();
        }
    }



    public static void update(Review review) throws SQLException {
        try (Connection conn = Database.getConnection()) {


            PreparedStatement statement = conn.prepareStatement("UPDATE Review SET description=?, name=?, updated_at=NOW() WHERE id = ?"); //updates value of the previous description in review

            statement.setString(1, escapeHtml(review.getDescription()));
            statement.setString(2, escapeHtml(review.getName()));
            statement.setLong(3, review.getId());

            statement.executeUpdate();
        }
    }



    public static void createReviewRecord(AmenityTypeMetricRecord record) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO ReviewMetricRecord (amenity_metric_id, review_id, value) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, record.getAmenityMetricId());
            ps.setLong(2, record.getReviewId());
            ps.setDouble(3, record.getValue());

            ps.executeUpdate();
        }
    }


    public static List<Review> getAllReviews(Long amenityId, Long currentUser, Boolean showHidden) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT *, COALESCE((" +        // if the value is empty, coalesce replaces any null value with 0
                            "SELECT SUM(value) " +
                            "FROM ReviewVote " +
                            "WHERE review_id = Review.id), 0) AS votes " +
                            (currentUser != null
                                    ? ", COALESCE((SELECT value FROM ReviewVote WHERE user_id = ? AND review_id = Review.id), 0) AS voted " //if not null, replace Vote value and pair with review and user ID
                                    : " "
                            ) +
                            "FROM Review WHERE amenity_id = ? " + ((showHidden.equals(Boolean.FALSE) ? " AND hidden = 0 " : " ")) +  // match review with amenityID
                            "ORDER BY created_at DESC"

            );
            int paramIndex = 1;
            if (currentUser != null) {
                statement.setLong(paramIndex++, currentUser);

            }
            statement.setLong(paramIndex++, amenityId);


            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Review review = fromResultSet(resultSet);
                review.setVotes(resultSet.getInt("votes"));
                if(currentUser != null){
                    review.setVoted(resultSet.getInt("voted"));

                }
                else{
                    review.setVoted(0);
                }
                reviews.add(review);

            }
        }
        return reviews;
    }

    public static List<String> getAllImagesForReview(Long reviewId) throws SQLException {
        ArrayList<String> urls = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT url FROM ReviewImage WHERE review_id = ?"
            );

            statement.setDouble(1, reviewId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                urls.add(resultSet.getString("url"));
            }
        }

        return urls;

    }

    public static List<String> getAllImagesForAmenity(Long amenityId) throws SQLException {
        ArrayList<String> urls = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT url FROM ReviewImage x LEFT JOIN Review y ON y.hidden = 0 AND x.review_id = y.id WHERE amenity_id = ?"
            );

            statement.setDouble(1, amenityId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                urls.add(resultSet.getString("url"));
            }

            return urls;
        }
    }

    public static void vote(Review review, User user, int value) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT value FROM ReviewVote WHERE review_id = ? AND user_id = ?" // Select current value info if user has made a review
            );
            statement.setLong(1, review.getId());
            statement.setLong(2, user.getId());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                statement = conn.prepareStatement("INSERT INTO ReviewVote (review_id, user_id, value) VALUES (?, ?, ?)"); // if nothing select it here
                statement.setLong(1, review.getId());
                statement.setLong(2, user.getId());
                statement.setInt(3, value);
                statement.executeUpdate();
                return;
            }

            int previousValue = resultSet.getInt(1);
            if (previousValue == value) {
                statement = conn.prepareStatement("DELETE FROM ReviewVote WHERE review_id = ? AND user_id = ?"); // if user clicks button, deletes value of their vote
                statement.setLong(1, review.getId());
                statement.setLong(2, user.getId());
                statement.executeUpdate();
            } else {
                statement = conn.prepareStatement("UPDATE ReviewVote SET value = ? WHERE review_id = ? AND user_id = ?"); //update value based on their new
                statement.setInt(1, value);
                statement.setLong(2, review.getId());
                statement.setLong(3, user.getId());
                statement.executeUpdate();
            }
        }
    }

    public static void updateReviewRecord(Long reviewId, Long amenityMetricId, int value) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE ReviewMetricRecord SET value = ? WHERE review_id = ? AND amenity_metric_id = ?"
            );
            statement.setInt(1, value);
            statement.setLong(2, reviewId);
            statement.setLong(3, amenityMetricId);
            statement.executeUpdate();
        }
    }

    public static Review getReviewByUserAndAmenity(Long id, Long amenityId) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Review WHERE user_id = ? AND amenity_id = ?"
            );
            statement.setLong(1, id);
            statement.setLong(2, amenityId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return fromResultSet(resultSet);
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Review> getReviewsByUser(User user, Long currentUser, Boolean showHidden) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT *, COALESCE((" +
                            "SELECT SUM(value) " +
                            "FROM ReviewVote " +
                            "WHERE review_id = Review.id), 0) AS votes " +
                            (currentUser != null
                                    ? ", COALESCE((SELECT value FROM ReviewVote WHERE user_id = ? AND review_id = Review.id), 0) AS voted "
                                    : " "
                            ) +
                            "FROM Review WHERE user_id = ?" + ((showHidden.equals(Boolean.FALSE) ? " AND hidden = 0 " : " ")) +
                            "ORDER BY created_at DESC"

            );

            int paramIndex = 1;
            if (currentUser != null) {
                statement.setLong(paramIndex++, currentUser);
            }
            statement.setLong(paramIndex++, user.getId());

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                Review review = fromResultSet(resultSet);
                review.setVotes(resultSet.getInt("votes"));
                if(currentUser != null){
                    review.setVoted(resultSet.getInt("voted"));
                }
                else{
                    review.setVoted(0);
                }
                reviews.add(review);
            }
        }

        return reviews;
    }
}

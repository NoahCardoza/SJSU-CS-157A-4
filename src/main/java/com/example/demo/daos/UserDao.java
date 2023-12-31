package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.User;
import com.example.demo.beans.entities.UserStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class UserDao {
    private static final String UPDATE_USER_VERIFIED = "UPDATE User SET verified = 1 WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM User WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM User";
    private static final String SELECT_FUZZY_SEARCH = "SELECT * FROM User WHERE username LIKE ? OR email LIKE ? OR normalized_email LIKE ?";
    private static final String INSERT = "INSERT INTO User (username, email, normalized_email, password, verified, name) VALUES (?, ?, ?, ?, ?, ?)";
    static UserDao instance = null;
    static public UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {}

    public Optional<User> get(Long id) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM User WHERE id = ?");
            statement.setDouble(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = fromResultSet(resultSet);
                user.setName(resultSet.getString("name"));
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    private User fromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setNormalizedEmail(resultSet.getString("normalized_email"));
        user.setAdministrator(resultSet.getBoolean("administrator"));
        user.setBanned(resultSet.getBoolean("banned"));
        user.setModerator(resultSet.getBoolean("moderator"));
        user.setPassword(resultSet.getString("password"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        user.setVerified(resultSet.getBoolean("verified"));
        user.setPrivateProfile(resultSet.getBoolean("private"));

        return user;
    }

    public Long create(User user) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, escapeHtml(user.getUsername()));
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getNormalizedEmail());
            ps.setString(4, user.getPassword());
            ps.setBoolean(5, user.isVerified() == null ? false : user.isVerified());
            ps.setString(6, escapeHtml(user.getName()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            user.setId(rs.getLong(1));

            // get the id of the newly created user
            return user.getId();
        }
    }

    public boolean isUnique(User user) throws SQLException {

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=? OR email=? OR normalized_email=?");

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getNormalizedEmail());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()){
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public void delete(User user) throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement ps = con.prepareStatement(DELETE_USER);
            ps.setLong(1, user.getId());
            ps.executeUpdate();
        }
    }

    public void verifyEmail(Long userId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement(UPDATE_USER_VERIFIED);
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    public void ban(Long userId) throws SQLException{
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement("UPDATE User SET banned = 1 WHERE id = ?");
            statement.setLong(1, userId);
            statement.executeUpdate();

        }
    }

    public void unban(Long userId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement("UPDATE User SET banned = 0 WHERE id = ?");
            statement.setLong(1, userId);
            statement.executeUpdate();

        }
    }

    public void promote(Long userId, String role) throws SQLException{
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement("UPDATE User SET " + role + " = 1 WHERE id = ?");
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    public void demote(Long userId, String role) throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement("UPDATE User SET " + role + " = 0 WHERE id = ?");
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    public List<User> fuzzySearch(String q) throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement(SELECT_FUZZY_SEARCH);
            statement.setString(1, "%" + q + "%");
            statement.setString(2, "%" + q + "%");
            statement.setString(3, "%" + q + "%");

            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = fromResultSet(resultSet);
                users.add(user);
            }
            return users;
        }
    }

    public List<User> getAll() throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement(SELECT_ALL);

            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = fromResultSet(resultSet);
                users.add(user);
            }
            return users;
        }
    }

    public UserStats getUserStats(User user) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("""
                SELECT (SELECT COUNT(*) AS reviews FROM Review WHERE user_id = ?) as reviews,
                       (SELECT COUNT(*) AS amenities FROM Amenity WHERE user_id = ?) as amenities,
                       (SELECT COUNT(*) AS locations FROM Location WHERE user_id = ?) as locations
            """);
            statement.setLong(1, user.getId());
            statement.setLong(2, user.getId());
            statement.setLong(3, user.getId());
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            UserStats userStats = new UserStats();
            userStats.setId(user.getId());
            userStats.setReviews(resultSet.getInt("reviews"));
            userStats.setAmenities(resultSet.getInt("amenities"));
            userStats.setLocations(resultSet.getInt("locations"));

            return userStats;
        }
    }

    public void update(User user) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("""
                UPDATE User SET
                    username = ?,
                    email = ?,
                    normalized_email = ?,
                    administrator = ?,
                    banned = ?,
                    moderator = ?,
                    password = ?,
                    verified = ?,
                    private = ?
                WHERE id = ?
            """);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getNormalizedEmail());
            statement.setBoolean(4, user.isAdministrator());
            statement.setBoolean(5, user.isBanned());
            statement.setBoolean(6, user.isModerator());
            statement.setString(7, user.getPassword());
            statement.setBoolean(8, user.isVerified());
            statement.setBoolean(9, user.getPrivateProfile());
            statement.setLong(10, user.getId());

            statement.executeUpdate();
        }
    }
}

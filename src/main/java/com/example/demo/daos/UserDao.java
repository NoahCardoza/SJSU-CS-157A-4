package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.User;
import jakarta.servlet.http.HttpSession;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Security.escapeHtml;

public class UserDao {
    static UserDao instance = null;
    static public UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {}

    public Optional<User> get(Long id) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM User WHERE id = ?");
        statement.setDouble(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
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



        return user;
    }

    public Optional<User> fromSession(HttpSession session) throws SQLException {
        Long userId = (Long) session.getAttribute("user_id");

        if (userId == null) return Optional.empty();

        return get((Long) session.getAttribute("user_id"));
    }


    public Long create(User user) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement("INSERT INTO User (username, email, normalized_email, password, verified) VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, escapeHtml(user.getUsername()));
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setBoolean(5, user.isVerified() == null ? false : user.isVerified());

        statement.executeUpdate();

        user.setId(Database.getLastInsertedId("User"));
        // get the id of the newly created user
        return user.getId();
    }

    public boolean isUnique(User user) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * FROM User WHERE username=? OR email=? OR normalized_email=?");

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getEmail());

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
            con.prepareStatement("DELETE FROM User WHERE id = ?" + user.getId()).executeUpdate();
        }
    }

    public void verifyEmail(Long userId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            PreparedStatement statement = con.prepareStatement("UPDATE User SET verified = 1 WHERE id = ?");
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }
}

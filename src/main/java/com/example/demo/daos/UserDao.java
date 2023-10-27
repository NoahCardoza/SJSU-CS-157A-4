package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.User;
import jakarta.servlet.http.HttpSession;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {
    static UserDao instance = null;
    static public UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {}

    public Optional<User> get(Long id) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("SELECT id, username, email, password FROM User WHERE id = ?");
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
        user.setPassword(resultSet.getString("password"));

        return user;
    }

    public Optional<User> fromSession(HttpSession session) throws SQLException {
        Long userId = (Long) session.getAttribute("user_id");

        if (userId == null) return Optional.empty();

        return get((Long) session.getAttribute("user_id"));
    }

    public Long create(User user) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement statement = conn.prepareStatement("INSERT INTO User (username, email, normalized_email, password) VALUES (?, ?, ?, ?)");
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());

        statement.executeUpdate();

        user.setId(Database.getInstance().getLastInsertedId("User"));
        // get the id of the newly created user
        return user.getId();
    }

    public boolean isUnique(User user) throws SQLException {
        Connection conn = Database.getInstance().getConnection();

        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * FROM User WHERE username=? OR email=?");

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()){
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public Optional get(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List getAll() throws SQLException {
        return null;
    }
}

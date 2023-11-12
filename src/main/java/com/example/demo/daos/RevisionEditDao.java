package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.RevisionEdit;

import java.sql.*;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.Revision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class RevisionEditDao {
    static RevisionEditDao instance = null;
    static public RevisionEditDao getInstance() {
        if (instance == null) {
            instance = new RevisionEditDao();
        }
        return instance;
    }

    private RevisionEditDao() {}

    private RevisionEdit fromResultSet(ResultSet resultSet) throws SQLException {
        RevisionEdit edit = new RevisionEdit();

        edit.setId(resultSet.getLong("id"));
        edit.setRevisionId(resultSet.getLong("revision_id"));
        edit.setTableName(resultSet.getString("table_name"));
        edit.setPrimaryKey(resultSet.getLong("primary_key"));
        edit.setColumnName(resultSet.getString("column_name"));
        edit.setPreviousValue(resultSet.getString("previous_value"));
        edit.setNewValue(resultSet.getString("new_value"));

        return edit;
    }

    public ArrayList<RevisionEdit> getAllByRevisionId(Long revisionId) throws SQLException {
        Connection conn = Database.getConnection();

        ArrayList<RevisionEdit> results = new ArrayList<>();

        if (conn == null) {
            return results;
        }

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM RevisionEdit WHERE revision_id = ?");
        statement.setLong(1, revisionId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            results.add(fromResultSet(resultSet));
        }

        return results;
    }

    public Long create(RevisionEdit edit) throws SQLException {
        PreparedStatement ps = Database.getConnection().prepareStatement(
                "INSERT INTO RevisionEdit (revision_id, table_name, primary_key, column_name, previous_value, new_value) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        ps.setLong(1, edit.getRevisionId());
        ps.setString(2, edit.getTableName());
        ps.setLong(3, edit.getPrimaryKey());
        ps.setString(4, edit.getColumnName());
        ps.setString(5, edit.getPreviousValue());
        ps.setString(6, edit.getNewValue());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        rs.next();

        edit.setId(rs.getLong(1));

        return edit.getId();
    }

    public ArrayList<Revision> getRevisionEdits(String tableName, Long primaryKey) throws SQLException {
        ArrayList<Revision> revisions = new ArrayList<>();
        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Revision WHERE table_name = ? AND primary_key = ?");
        statement.setString(1, tableName);
        statement.setLong(2, primaryKey);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Revision revision = new Revision();
            revision.setId(resultSet.getLong("id"));
            revision.setUserId(resultSet.getLong("user_id"));
            revision.setTableName(resultSet.getString("table_name"));
            revision.setPrimaryKey(resultSet.getLong("primary_key"));
            revision.setCreatedAt(resultSet.getTimestamp("created_at"));
            revision.setEdits(RevisionEditDao.getInstance().getAllByRevisionId(revision.getId()));

            revisions.add(revision);
        }

        return revisions;
    }
    public ArrayList<Revision> getRevisionsForLocation(Long locationId) throws SQLException {
        return getRevisionEdits("Location", locationId);
    }

    public void vote(Long revisionId, Long id, int value) throws SQLException {

        Connection conn = Database.getConnection();
        PreparedStatement statement = conn.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM RevisionVote WHERE revision_id = ? AND user_id = ?)"
        );
        statement.setLong(1, revisionId);
        statement.setLong(2, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if (resultSet.getBoolean(1)) {
            statement = conn.prepareStatement("UPDATE RevisionVote SET value = ? WHERE revision_id = ? AND user_id = ?");
            statement.setInt(1, value);
            statement.setLong(2, revisionId);
            statement.setLong(3, id);
            statement.executeUpdate();
            return;
        } else {
            statement = conn.prepareStatement("INSERT INTO RevisionVote (revision_id, user_id, value) VALUES (?, ?, ?)");
            statement.setLong(1, revisionId);
            statement.setLong(2, id);
            statement.setInt(3, value);
            statement.executeUpdate();
        }
    }
}

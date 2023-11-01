package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.RevisionEdit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        edit.setTable(resultSet.getString("table_name"));
        edit.setPrimaryKey(resultSet.getLong("primary_key"));
        edit.setColumn(resultSet.getString("column_name"));
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
        ps.setString(2, edit.getTable());
        ps.setLong(3, edit.getPrimaryKey());
        ps.setString(4, edit.getColumn());
        ps.setString(5, edit.getPreviousValue());
        ps.setString(6, edit.getNewValue());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        rs.next();

        edit.setId(rs.getLong(1));

        return edit.getId();
    }
}

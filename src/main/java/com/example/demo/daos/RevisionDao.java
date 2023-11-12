package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.Revision;
import com.example.demo.beans.entities.RevisionEdit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RevisionDao {
    static RevisionDao instance = null;
    static public RevisionDao getInstance() {
        if (instance == null) {
            instance = new RevisionDao();
        }
        return instance;
    }

    private RevisionDao() {}
    
    private Revision fromResultSet(ResultSet resultSet) throws SQLException {
        Revision revision = new Revision();

        revision.setId(resultSet.getLong("id"));
        revision.setUserId(resultSet.getLong("user_id"));
        revision.setTableName(resultSet.getString("table_name"));
        revision.setPrimaryKey(resultSet.getLong("primary_key"));
        revision.setCreatedAt(resultSet.getTimestamp("created_at"));

        return revision;
    }

    public Optional<Revision> get(long id) throws SQLException {
        PreparedStatement ps = Database.getConnection().prepareStatement("SELECT * FROM Revision WHERE id = ?");
        ps.setLong(1, id);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return Optional.of(fromResultSet(resultSet));
        }
        return Optional.empty();
    }

    public List<Revision> getAll() throws SQLException {
        return null;
    }

    public Long create(Revision revision) throws SQLException {
        PreparedStatement ps = Database.getConnection().prepareStatement(
                "INSERT INTO Revision (user_id, table_name, primary_key) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );

        ps.setLong(1, revision.getUserId());
        ps.setString(2, revision.getTableName());
        ps.setLong(3, revision.getPrimaryKey());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        rs.next();

        revision.setId(rs.getLong(1));

        return revision.getId();
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


    public Long createRevisionForLocationEdit(Long userId, Location prev, Location next) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> preValue = new ArrayList<>();
        ArrayList<String> nextValue = new ArrayList<>();

        if (!prev.getName().equals(next.getName())) {
            columns.add("name");
            preValue.add(prev.getName());
            nextValue.add(next.getName());
        }

        if (!prev.getDescription().equals(next.getDescription())) {
            columns.add("description");
            preValue.add(prev.getDescription());
            nextValue.add(next.getDescription());
        }

        if (!prev.getAddress().equals(next.getAddress())) {
            columns.add("address");
            preValue.add(prev.getAddress());
            nextValue.add(next.getAddress());
        }

        if (!prev.getLongitude().equals(next.getLongitude())) {
            columns.add("longitude");
            preValue.add(prev.getLongitude().toString());
            nextValue.add(next.getLongitude().toString());
        }

        if (!prev.getLatitude().equals(next.getLatitude())) {
            columns.add("latitude");
            preValue.add(prev.getLatitude().toString());
            nextValue.add(next.getLatitude().toString());
        }

        if (columns.isEmpty()) {
            return null;
        }

        Revision revision = new Revision();
        revision.setUserId(userId);
        revision.setTableName("Location");
        revision.setPrimaryKey(prev.getId());
        Long revisionId = RevisionDao.getInstance().create(revision);

        for (int i = 0; i < columns.size(); i++) {
            RevisionEdit edit = new RevisionEdit();
            edit.setRevisionId(revisionId);

            edit.setTableName("Location");
            edit.setPrimaryKey(prev.getId());
            edit.setColumnName(columns.get(i));
            edit.setPreviousValue(preValue.get(i));
            edit.setNewValue(nextValue.get(i));
            RevisionEditDao.getInstance().create(edit);
        }

        return revisionId;
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

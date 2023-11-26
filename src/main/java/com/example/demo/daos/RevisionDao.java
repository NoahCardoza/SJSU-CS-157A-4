package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.Revision;
import com.example.demo.beans.entities.RevisionEdit;
import com.example.demo.beans.entities.User;

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
        revision.setReverted(resultSet.getBoolean("reverted"));
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

    public ArrayList<Revision> getEntityRevisions(String tableName, Long primaryKey, Long currentUser) throws SQLException {
        ArrayList<Revision> revisions = new ArrayList<>();
        Connection conn = Database.getConnection();

        PreparedStatement statement = conn.prepareStatement(
                "SELECT *, COALESCE((" +
                        "SELECT SUM(value) " +
                        "FROM RevisionVote " +
                        "WHERE revision_id = Revision.id), 0) AS votes " +
                        (currentUser != null
                                ? ", COALESCE((SELECT value FROM RevisionVote WHERE user_id = ? AND revision_id = Revision.id), 0) AS voted "
                                : " "
                        ) +
                    "FROM Revision " +
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
            Revision revision = fromResultSet(resultSet);
            revision.setEdits(RevisionEditDao.getInstance().getAllByRevisionId(revision.getId()));
            revision.setVotes(resultSet.getInt("votes"));
            if (currentUser != null) {
                revision.setVoted(resultSet.getInt("voted"));
            }
            revisions.add(revision);
        }

        return revisions;
    }

    public ArrayList<Revision> getRevisionsForLocation(Long locationId, Long currentUser) throws SQLException {
        return getEntityRevisions("Location", locationId, currentUser);
    }

    public void getEditsForRevision(Revision revision) throws SQLException {
        revision.setEdits(RevisionEditDao.getInstance().getAllByRevisionId(revision.getId()));
    }

    public void getUserForRevision(Revision revision) throws SQLException {
        Optional<User> user = UserDao.getInstance().get(revision.getUserId());
        if (user.isPresent()) {
            revision.setUser(user.get());
        } else {
            revision.setUser(null);
            System.out.println("User not found for revision " + revision.getId());
        }
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
                "SELECT value FROM RevisionVote WHERE revision_id = ? AND user_id = ?"
        );
        statement.setLong(1, revisionId);
        statement.setLong(2, id);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement = conn.prepareStatement("INSERT INTO RevisionVote (revision_id, user_id, value) VALUES (?, ?, ?)");
            statement.setLong(1, revisionId);
            statement.setLong(2, id);
            statement.setInt(3, value);
            statement.executeUpdate();
            return;
        } else {
            int previousValue = resultSet.getInt(1);
            if (previousValue == value) {
                statement = conn.prepareStatement("DELETE FROM RevisionVote WHERE revision_id = ? AND user_id = ?");
                statement.setLong(1, revisionId);
                statement.setLong(2, id);
                statement.executeUpdate();
            } else {
                statement = conn.prepareStatement("UPDATE RevisionVote SET value = ? WHERE revision_id = ? AND user_id = ?");
                statement.setInt(1, value);
                statement.setLong(2, revisionId);
                statement.setLong(3, id);
                statement.executeUpdate();
            }
        }
    }

    public void revert(Revision revision) throws SQLException {
        // TODO: merge if edits have been made since

        getEditsForRevision(revision);

        for (RevisionEdit edit : revision.getEdits()) {
            String tableName = edit.getTableName();
            String columnName = edit.getColumnName();
            String newValue = edit.getPreviousValue();
            Long primaryKey = edit.getPrimaryKey();

            PreparedStatement statement = Database.getConnection().prepareStatement(
                    "UPDATE " + tableName + " SET " + columnName + " = ? WHERE id = ?"
            );
            statement.setString(1, newValue);
            statement.setLong(2, primaryKey);
            statement.executeUpdate();
        }

        // TODO: track who reverted the revision
        revision.setReverted(true);

        PreparedStatement statement = Database.getConnection().prepareStatement(
                "UPDATE Revision SET reverted = ? WHERE id = ?"
        );
        statement.setBoolean(1, true);
        statement.setLong(2, revision.getId());
        statement.executeUpdate();
    }
}

package com.example.demo.daos;

import com.example.demo.Database;
import com.example.demo.beans.MinMax;
import com.example.demo.beans.entities.AmenityTypeAttribute;
import com.example.demo.beans.entities.Location;
import com.example.demo.beans.entities.Revision;
import com.example.demo.beans.entities.RevisionEdit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RevisionDao implements Dao<Revision> {
    static RevisionDao instance = null;
    static public RevisionDao getInstance() {
        if (instance == null) {
            instance = new RevisionDao();
        }
        return instance;
    }

    private RevisionDao() {}

    private AmenityTypeAttribute fromResultSet(ResultSet resultSet) throws SQLException {
        AmenityTypeAttribute amenityTypeAttribute = new AmenityTypeAttribute();

        amenityTypeAttribute.setId(resultSet.getLong("id"));
        amenityTypeAttribute.setName(resultSet.getString("name"));
        amenityTypeAttribute.setAmenityTypeId(resultSet.getLong("amenity_type_id"));
        amenityTypeAttribute.setIcon(resultSet.getString("icon"));
        amenityTypeAttribute.setType(resultSet.getString("type"));

        return amenityTypeAttribute;
    }

    @Override
    public Optional<Revision> get(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Revision> getAll() throws SQLException {
        return null;
    }

    @Override
    public Long create(Revision revision) throws SQLException {
        PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(
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

    public ArrayList<Revision> getRevisionsForLocation(Long locationId) throws SQLException {
        ArrayList<Revision> revisions = new ArrayList<>();
        Connection conn = Database.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Revision WHERE table_name = 'Location' AND primary_key = ?");
        statement.setLong(1, locationId);
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
            edit.setTable("Location");
            edit.setPrimaryKey(prev.getId());
            edit.setColumn(columns.get(i));
            edit.setPreviousValue(preValue.get(i));
            edit.setNewValue(nextValue.get(i));
            RevisionEditDao.getInstance().create(edit);
        }

        return revisionId;
    }
}

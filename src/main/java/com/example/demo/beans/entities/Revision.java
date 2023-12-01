package com.example.demo.beans.entities;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Revision {
    private Long id;
    private String tableName;
    private Long primaryKey;
    private Long userId;
    private Boolean reverted;
    private Timestamp createdAt;

    // utility field
    private List<RevisionEdit> edits;
    private User user;
    private Integer votes;
    /**
     * 0: not voted
     * 1: upvoted
     * -1: downvoted
     */
    private Integer voted;

    public Revision() {
        this.edits = new ArrayList<>();
    }

    public void setEdits(ArrayList<RevisionEdit> edits) {
        this.edits = edits;
    }

    public List<RevisionEdit> getEdits() {
        return edits;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean isReverted() {
        return reverted;
    }

    public void setReverted(Boolean reverted) {
        this.reverted = reverted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "id=" + id +
                ", userId=" + userId +
                ", reverted=" + reverted +
                ", createdAt=" + createdAt +
                ", voted=" + voted +
                ", votes=" + votes +
                ", edits=" + edits +
                '}';
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }

    public int getVoted() {
        return voted;
    }
}

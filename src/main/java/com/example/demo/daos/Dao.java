package com.example.demo.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(long id) throws SQLException;

    List<T> getAll() throws SQLException;

//    void save(T t);

    void create(T t) throws SQLException;
//
//    void update(T t);
//
//    void delete(T t);
}


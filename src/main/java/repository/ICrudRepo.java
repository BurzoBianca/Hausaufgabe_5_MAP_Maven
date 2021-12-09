package repository;


import controller.Exception_Null;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public interface ICrudRepo <T> {

    T create(T obj) throws IOException, SQLException;

    List<T> getAll();

    T update(T obj) throws IOException, SQLException, Exception_Null;

    void delete(T obj) throws IOException, SQLException, Exception_Null;
}
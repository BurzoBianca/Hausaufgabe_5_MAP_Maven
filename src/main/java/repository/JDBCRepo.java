package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class JDBCRepo<T> implements ICrudRepo<T> {
    protected List<T> repoList;
    protected Connection connection;

    public JDBCRepo(Connection connection) throws SQLException {
        this.repoList = new ArrayList<>();
        this.connection = connection;
        repoList = read();
    }


    public abstract List<T>  read() throws SQLException;


    public abstract T create(T obj) throws SQLException;


    public List<T> getAll(){return repoList;}

    public abstract T update(T obj) throws SQLException;

    public abstract void delete(T obj) throws SQLException;


}
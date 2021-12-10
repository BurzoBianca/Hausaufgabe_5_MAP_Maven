package repository;

import classes.*;
import controller.*;
import static org.testng.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherRepo_JDBCTest {

    TeacherRepo_JDBC teacherRepo_jdbc;
    Teacher teacher1;
    Teacher teacher2;
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hsg_5_map";
    static final String USER = "BurzoBianca";
    static final String PASS = "Biancaare3pene!";

    public static void resetDatabase(Statement statement) throws SQLException {
        statement.executeUpdate("DELETE FROM teacher WHERE id NOT IN (1,2);");
    }

    @BeforeEach
    void init(){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = connection.createStatement();

            resetDatabase(stmt);

            teacherRepo_jdbc = new TeacherRepo_JDBC((Statement) connection);

            teacher1 = teacherRepo_jdbc.getAll().get(0);
            teacher2 = teacherRepo_jdbc.getAll().get(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException {
        Teacher teacher3 = new Teacher(13,"Madalina", "Dicu");
        teacherRepo_jdbc.create(teacher3);
        List<Teacher> expectedTeachers = new ArrayList<>(List.of(teacher1,teacher2,teacher3));
        assertEquals(expectedTeachers,teacherRepo_jdbc.getAll());

    }

    @Test
    void delete() throws SQLException {
        Teacher teacher3 = new Teacher(13,"Madalina", "Dicu");
        teacherRepo_jdbc.create(teacher3);
        teacher3.addCourse(4);
        teacher3.addCourse(5);
        teacherRepo_jdbc.delete(teacher3);

        List<Teacher> expectedTeachers = new ArrayList<>(List.of(teacher1, teacher2));
        assertEquals(expectedTeachers, teacherRepo_jdbc.getAll());
    }

}
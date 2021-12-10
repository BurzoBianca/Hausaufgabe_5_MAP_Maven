package repository;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import repository.*;
import controller.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.testng.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseRepo_JDBCTest {

    Course course1;
    Course course2;
    Course course3;

    CourseRepo_JDBC courseRepo_jdbc;

    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hsg_5_map";
    static final String USER = "BurzoBianca";
    static final String PASS = "Biancaare3pene!";

    public static void resetDatabase(Statement statement) throws SQLException {
        statement.executeUpdate("DELETE FROM enrolled_students;");
        statement.executeUpdate("DELETE FROM course WHERE id NOT IN (1,2,3);");
    }

    @BeforeEach
    void init(){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = (Statement) connection.createStatement();

            resetDatabase(stmt);

            courseRepo_jdbc = new CourseRepo_JDBC((Statement) connection);

            course1 = courseRepo_jdbc.getAll().get(0);
            course2 = courseRepo_jdbc.getAll().get(1);
            course3 = courseRepo_jdbc.getAll().get(2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException {
        Course course4 = new Course(14,"OOP", 12, 2, 5);
        courseRepo_jdbc.create(course4);
        List<Course> expectedCourses = new ArrayList<>(List.of(course1,course2,course3,course4));

        assertEquals(expectedCourses,courseRepo_jdbc.getAll());
    }

    @Test
    void update() throws SQLException {
        Course course4  = new Course(14,"OOP", 12, 2, 5);
        courseRepo_jdbc.create(course4);
        course4.addStudent(1);

        //adaug student nou
        courseRepo_jdbc.update(course4);
        List<Integer> expectedStudents =  new ArrayList<>(List.of(1));
        assertEquals(expectedStudents, courseRepo_jdbc.getAll().get(3).getStudentsEnrolledId());

        course4.addStudent(2);
        courseRepo_jdbc.update(course4);

        //sterg 2 studenti
        course4.removeStudent(1);
        course4.removeStudent(2);
        courseRepo_jdbc.update(course4);
        List<Integer> expectedStudents2 =  new ArrayList<>(List.of());
        assertEquals(expectedStudents2, courseRepo_jdbc.getAll().get(3).getStudentsEnrolledId());

    }

    @Test
    void delete() throws SQLException {
        Course course4  = new Course(14,"OOP", 12, 2, 5);
        courseRepo_jdbc.create(course4);
        course4.addStudent(1);
        courseRepo_jdbc.update(course4);

        courseRepo_jdbc.delete(course4);
        List<Course> expectedCourses = new ArrayList<>(List.of(course1,course2,course3));
        assertEquals(expectedCourses,courseRepo_jdbc.getAll());
    }
}
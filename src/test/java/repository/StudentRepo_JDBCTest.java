package repository;

import classes.*;
import controller.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.*;

import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentRepo_JDBCTest {

    StudentRepo_JDBC studentRepo_jdbc;
    Student student1;
    Student student2;
    Student student3;
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hsg_5_map";
    static final String USER = "BurzoBianca";
    static final String PASS = "Biancaare3pene!";

    public static void resetDatabase(Statement stmt) throws SQLException {
        stmt.executeUpdate("DELETE FROM student WHERE id NOT IN (1,2,3);");
    }

    @BeforeEach
    void init(){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();

            resetDatabase(statement);

            studentRepo_jdbc = new StudentRepo_JDBC((Statement) connection);

            student1 = this.studentRepo_jdbc.getAll().get(0);
            student2 = this.studentRepo_jdbc.getAll().get(1);
            student3 = this.studentRepo_jdbc.getAll().get(2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException {
        Student student4 = new Student(4,"Bianca", "Burzo", 101);
        studentRepo_jdbc.create(student4);
        List<Student> expepectedStudents = new ArrayList<>(List.of(student1,student2,student3,student4));

        assertEquals(expepectedStudents,studentRepo_jdbc.getAll());
    }

    @Test
    void delete() throws SQLException {
        Student studnet4 = new Student(4,"Bianca", "Burzo", 101);
        studentRepo_jdbc.create(studnet4);
        Course course1 = new Course(1,"MAP", 11,2, 10 );
        Pair coursePair = new Pair(course1.getId(), course1.getCredits());
        studnet4.setEnrolledCourses(List.of(coursePair));

        studentRepo_jdbc.delete(studnet4);
        List<Student> expepectedStudents = new ArrayList<>(List.of(student1,student2,student3));

        assertEquals(expepectedStudents,studentRepo_jdbc.getAll());
    }
}

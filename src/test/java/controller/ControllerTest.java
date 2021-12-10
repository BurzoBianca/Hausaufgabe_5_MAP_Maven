package controller;

import repository.*;
import classes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    Controller controller;
    Teacher teacher1;
    Teacher teacher2;
    Course course1;
    Course course2;
    Course course3;
    Student student1;
    Student student2;
    Student student3;

    CourseRepo_JDBC courseRepo_jdbc;
    StudentRepo_JDBC studentRepo_jdbc;
    TeacherRepo_JDBC teacherRepo_jdbc;


    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/datenbank_hausaufgabe5_map";
    static final String USER = "BurzoBianca";
    static final String PASS = "Biancaare3pene!";

    public static void resetDatabase(Statement stmt) throws SQLException {
        stmt.executeUpdate("DELETE FROM enrolled_students;");
        stmt.executeUpdate("UPDATE student SET totalECTS = " + 0 + ";");
        stmt.executeUpdate("DELETE FROM course;");
        stmt.executeUpdate("INSERT INTO course VALUES (1,'MAP',11,2, 10), (2,'BD', 11, 2, 10), (3,'LP', 12, 2, 11)");


    }
    @BeforeEach
    void init(){
        try(Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement()){

            resetDatabase(stmt);

            teacherRepo_jdbc = new TeacherRepo_JDBC(stmt);
            studentRepo_jdbc = new StudentRepo_JDBC(stmt);
            courseRepo_jdbc = new CourseRepo_JDBC(stmt);

            controller = new Controller(courseRepo_jdbc,studentRepo_jdbc,teacherRepo_jdbc);
            teacher1 = teacherRepo_jdbc.getAll().get(0);
            teacher2 = teacherRepo_jdbc.getAll().get(1);

            course1 = this.courseRepo_jdbc.getAll().get(0);
            course2 = this.courseRepo_jdbc.getAll().get(1);
            course3  = this.courseRepo_jdbc.getAll().get(2);

            student1 = this.studentRepo_jdbc.getAll().get(0);
            student2 = this.studentRepo_jdbc.getAll().get(1);
            student3 = this.studentRepo_jdbc.getAll().get(2);

            controller.register(course1,student1);
            controller.register(course1,student2);

            controller.register(course2,student1);
            controller.register(course2,student3);

            controller.register(course3,student3);

        } catch (SQLException | Exception_Input exeption) {
            exeption.printStackTrace();
        }
    }

    @Test
    void register() throws IOException, SQLException{

        //check the effect on the students
        List<Pair> expectedCourses = new ArrayList<>(Arrays.asList(new Pair(course1.getId(),course1.getCredits()),new Pair(course2.getId(),course2.getCredits())));
        assertEquals(expectedCourses,student1.getEnrolledCourses());

        List<Integer> expectedStudents = new ArrayList<>(Arrays.asList(student1.getId(), student3.getId()));
        assertEquals(expectedStudents, course2.getStudentsEnrolledId());

        //cand studentul nu exista
        Student student4 = new Student(14,"Dragos", "Baba", 300);
        List<Pair> expectedCourses2 = new ArrayList<>(List.of());
        List<Integer> expectedStudents2 = new ArrayList<>(List.of(student3.getId()));
        try{
            controller.register(course3,student4);
        }
        catch (Exception_Input e){
            assertEquals(expectedCourses2,student4.getEnrolledCourses());
            assertEquals(expectedStudents2, course3.getStudentsEnrolledId());
        }

        //cand cursul nu exista
        Course course4  = new Course(14, "OOP", 12, 2, 5);
        List<Integer> expectedStudents3 = new ArrayList<>(List.of());
        try{
            controller.register(course4,student1);
        }
        catch (Exception_Input e){
            assertEquals(expectedCourses,student1.getEnrolledCourses());
            assertEquals(expectedStudents3, course4.getStudentsEnrolledId());

        }
    }

    @Test
    void retriveCoursesWithFreePlaces() {
        List<Course> freePlacesCourses = controller.retriveCoursesWithFreePlaces();
        List<Course> expectedCourses = new ArrayList<>(List.of(course3));
        assertEquals(expectedCourses,freePlacesCourses);
    }

    @Test
    void retrieveStudentsEnrolledForACourse() throws Exception_Input {
        List<Integer> enrolledStudents = controller.retrieveStudentsEnrolledForACourse(course2);
        List<Integer> expectedStudents = new ArrayList<>(Arrays.asList(student1.getId(), student3.getId()));
        assertEquals(expectedStudents,enrolledStudents);
    }

    @Test
    void getAllCourses() {
        List<Course> allCourses = controller.getAllCourses();
        List<Course> expectedCourses = new ArrayList<>(Arrays.asList(course1, course2, course3));
        assertEquals(expectedCourses,allCourses);
    }

    @Test
    void sortStudents() {
        List<Student> obtainedStudents = controller.sortStudents();
        List<Student> expectedStudents = new ArrayList<>(Arrays.asList(student1,student2,student3));

        assertEquals(expectedStudents,obtainedStudents);
    }

    @Test
    void sortCourses() {
        List<Course> obtainedCourses = controller.sortCourses();
        List<Course> expectedCourses= new ArrayList<>(Arrays.asList(course1, course2, course3));

        assertEquals(expectedCourses,obtainedCourses);
    }

    @Test
    void filterStudents() {
        List<Student> obtainedStudents = controller.filterStudents();
        List<Student> expectedStudents = new ArrayList<>(List.of(student2));

        assertEquals(expectedStudents,obtainedStudents);
    }

    @Test
    void filterCourses() {
        List<Course> obtainedCourses = controller.filterCourses();
        List<Course> expectedCourses= new ArrayList<>(List.of(course3));

        assertEquals(expectedCourses,obtainedCourses);
    }
}
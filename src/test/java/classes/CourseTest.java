package classes;
import repository.*;
import controller.*;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseTest {

    Controller controller;
    Student student1;
    Student student2;
    Student student3;
    Course course1;
    Course course2;
    Course course3;
    Teacher teacher1;

    StudentRepo_JDBC studentRepo_jdbc;
    TeacherRepo_JDBC teacherRepo_jdbc;
    CourseRepo_JDBC courseRepo_jdbc;

    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/datenbank_hausaufgabe5_map";
    static final String USER = "BurzoBianca";
    static final String PASS = "Biancaare3pene!";


    public static void resetDb(Statement stmt) throws SQLException {
        stmt.executeUpdate("DELETE FROM enrolled_students;");
        stmt.executeUpdate("UPDATE student SET totalECTS= "+ 0 + ";");
        stmt.executeUpdate("DELETE FROM course;");
        stmt.executeUpdate("INSERT INTO course VALUES (1,'MAP',11,2,10), (2,'BD',11,2,10), (3,'LP',12,2,11)");
    }

    @BeforeEach
    void init() {

        try(Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement()){

            resetDb(stmt);

            teacherRepo_jdbc = new TeacherRepo_JDBC(con);
            studentRepo_jdbc = new StudentRepo_JDBC(con);
            courseRepo_jdbc = new CourseRepo_JDBC(con);

            controller = new Controller(courseRepo_jdbc,studentRepo_jdbc,teacherRepo_jdbc);
            teacher1= teacherRepo_jdbc.getAll().get(0);

            course1 = this.courseRepo_jdbc.getAll().get(0);
            course2 = this.courseRepo_jdbc.getAll().get(1);
            course3 = this.courseRepo_jdbc.getAll().get(2);


            student1 = this.studentRepo_jdbc.getAll().get(0);
            student2= this.studentRepo_jdbc.getAll().get(1);
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

       /* Teacher t1 = new Teacher(11,"Carmen", "Matei");
        course = new Course(1,"MAP", 11, 2, 10);
        t1.addCourse(course.getId());

        Student s1 = new Student(1,"Catalina", "Vasiu", 110);
        Student s2 = new Student(1,"Victor", "Santa", 125);
        Student s3 = new Student(3,"Darius", "Oros", 118); */


    @Test
    void register() throws IOException, SQLException{

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
        Course course4  = new Course(4, "OOP", 16, 2, 5);
        List<Integer> expectedStudents3 = new ArrayList<>(List.of());
        try{
            controller.register(course4,student1);
        }
        catch (Exception_Input e){
            assertEquals(expectedCourses,student1.getEnrolledCourses());
            assertEquals(expectedStudents3, course4.getStudentsEnrolledId());

        }
    }

}
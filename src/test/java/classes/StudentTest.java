package classes;

import controller.*;
import org.junit.jupiter.api.Test;
import controller.Exception_LimitECTS;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student1;
    private Course course1;
    private Course course2;

    @BeforeEach
    void init(){

        Teacher teacher1 = new Teacher(11,"Carmen", "Matei");
        course1 = new Course(1,"MAP", 11,2, 10 );
        teacher1.addCourse(course1.getId());

        course2 = new Course(2,"BD", 11, 2, 8 );
        teacher1.addCourse(course2.getId());

        student1 = new Student(1,"Catalina", "Vasiu", 110);
        student1.addCourse(course1);

    }

    @Test
    void addCourse() {

        student1.addCourse(course2);
        List<Course> expectedCourses = new ArrayList<>(Arrays.asList(course1,course2));
        assertEquals(expectedCourses,student1.getEnrolledCourses());
        assertEquals(18,student1.getTotalCredits());
    }

    @Test
    void removeCourse() throws Exception_Input {

        student1.addCourse(course2);
        student1.removeCourse(course1);
        List<Course> expectedCourses = new ArrayList<>(Arrays.asList(course2));
        assertEquals(expectedCourses, student1.getEnrolledCourses());
        assertEquals(8, student1.getTotalCredits());
    }

    @Test
    void updateCredits() throws Exception_Input {

        student1.addCourse(course2);
        student1.updateCredits(course1,8);
        assertEquals(16,student1.getTotalCredits());

        try {
            student1.updateCredits(course2,25);
        }
        catch (Exception_LimitECTS e){
            List<Course> expectedCourses = new ArrayList<>(Arrays.asList(course1));
            assertEquals(expectedCourses, student1.getEnrolledCourses());
            assertEquals(15,student1.getTotalCredits());
        }
    }
}

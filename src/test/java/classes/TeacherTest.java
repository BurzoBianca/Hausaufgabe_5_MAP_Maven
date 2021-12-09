package classes;

import controller.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    Teacher teacher1;
    Course course1;
    Course course2;

    @BeforeEach
    void init() {
        teacher1 = new Teacher(11,"Carmen", "Matei");
        course1 = new Course(1,"MAP", 11, 2, 10);
        course2 = new Course(2,"BD", 11, 2, 8);
        teacher1.addCourse(course1.getId());
    }


    @Test
    void addCourse() {

        teacher1.addCourse(course2.getId());
        List<Course> expectedCourses = new ArrayList<>(Arrays.asList(course1,course2));
        assertEquals(expectedCourses,teacher1.getCourses());
        try{
            teacher1.addCourse(course1.getId());
        }
        finally {
            assertEquals(expectedCourses, teacher1.getCourses());
        }
    }

    @Test
    void removeCourse() throws Exception_Input {

        teacher1.addCourse(course2.getId());
        teacher1.removeCourse(course1.getId());
        List<Course> expectedCourses = new ArrayList<>(Arrays.asList(course2));
        assertEquals(expectedCourses,teacher1.getCourses());

        try{
            teacher1.removeCourse(course1.getId());
        }
        finally {
            assertEquals(expectedCourses, teacher1.getCourses());
        }
    }
}
package classes;

import controller.Exception_AlreadyExists;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import controller.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Persoana care preda la universitate
 * Poate preda mai multe cursuri
 */
public class Teacher extends Person{
    private List<Integer> courses;

    public Teacher(int id, String firstName, String lastName) {
        super(id, firstName, lastName);
        this.courses = new ArrayList<>();
    }

    public Teacher(int id, String firstName, String lastName, List<Integer> courses) {
        super(id, firstName, lastName);
        this.courses = courses;
    }


    public Teacher(){
        super();
    }


    public void addCourse(int courseId) throws Exception_AlreadyExists {
        if(courses.contains(courseId)){
            throw new Exception_AlreadyExists("Course already existing in the teachers list");
        }
        courses.add(courseId);
    }

    public void removeCourse(int courseId) throws Exception_Input {
        if(!courses.contains(courseId)){
            throw new Exception_Input("Course does not exist in the teachers list");
        }
        courses.remove(Integer.valueOf(courseId));
    }

    public List<Integer> getCourses() {
        return courses;
    }

    public void setCourses(List<Integer> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + getId()  +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", courses=" + courses +
                '}';
    }
}
package classes;
import controller.*;

import java.util.*;

/**
 *
 */
public class Course implements Comparable<Course> {
    private int id;
    private String name;
    private int teacherId;
    private int maxEnrollement;
    private List<Integer> studentsEnrolledId;
    private int credits;

    public Course(int id,String name, int teacherId, int maxEnrollement, int credits) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.maxEnrollement = maxEnrollement;
        this.credits = credits;
        this.studentsEnrolledId = new ArrayList<Integer>();
    }

    public Course(int id, int credits){
        this.id = id;

        this.credits = credits;
    }


    public Course(){}


    public void addStudent(int studentId) throws Exception_AlreadyExists, Exception_MaxLCurs{
        if(studentsEnrolledId.contains(studentId)){
            throw new Exception_AlreadyExists("Already registered to this course");
        }
        else
        if(studentsEnrolledId.size() == maxEnrollement){
            throw new Exception_MaxLCurs("The course has no places available");
        }
        studentsEnrolledId.add(studentId);
    }

    public void removeStudent(int studentId) throws Exception_AlreadyExists {
        if(!studentsEnrolledId.contains(studentId)){
            throw new Exception_AlreadyExists("Not registered to this course");
        }

        studentsEnrolledId.remove(studentId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdTeacher() {
        return teacherId;
    }

    public void setIdTeacher(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxEnrollement() {
        return maxEnrollement;
    }

    public void setMaxEnrollement(int maxEnrollement) {
        this.maxEnrollement = maxEnrollement;
    }

    public List<Integer> getStudentsEnrolledId() {
        return studentsEnrolledId;
    }

    public void setStudentsEnrolledId(List<Integer> studentsEnrolledId) {
        this.studentsEnrolledId = studentsEnrolledId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id = "+ id +
                ", name='" + name + '\'' +
                ", idTeacher=" + teacherId+
                ", maxEnrollment=" + maxEnrollement +
                ", studentsEnrolledId=" + studentsEnrolledId +
                ", credits=" + credits +
                '}';
    }


    @Override
    public int compareTo(Course course2) {
        return this.getName().compareTo(course2.getName());
    }

    public boolean contains(Course course) {
        return contains(course);
    }

    public boolean containsst(Student student) {
        return containsst(student);
    }

    public int getTeacher() {
        return getIdTeacher();
    }

}
package classes;

import controller.Exception_AlreadyExists;
import controller.Exception_Input;
import controller.Exception_LimitECTS;
//import com.mysql.cj.conf.ConnectionUrlParser;

import java.util.ArrayList;
import java.util.List;

/**
 * O persoana inscrisa la facultate
 * are un id si un numar de credite, totalul cursurilor la care este inscris
 * poate avea max 30 ects
 * poate fi inscris la mai multe cursuri
 */
public class Student extends Person implements Comparable<Student> {
    private long studentId;
    private int totalCredits;
    private List<Pair> enrolledCourses;

    public Student(int id, String firstName, String lastName, int totalCredits) {
        super(id, firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = totalCredits;
        this.enrolledCourses = new ArrayList<>();
    }

    public Student(int id, String firstName, String lastName, long studentId, int totalCredits, List<Pair> enrolledCourses) {
        super(id, firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = totalCredits;
        this.enrolledCourses = enrolledCourses;
    }

    public Student(int id, String firstName, String lastName, long studentId) {
        super(id, firstName, lastName);
        this.studentId = studentId;
        this.enrolledCourses = new ArrayList<>();
    }


    public Student(){super();}

    public void addCourse(Course course) throws Exception_LimitECTS, Exception_AlreadyExists {
        Pair newCoursePair = new Pair(course.getId(),course.getCredits());
        if(enrolledCourses.stream().anyMatch(pair -> pair.getCourseId() == course.getId())){
            throw new Exception_AlreadyExists("Course already existing in the student's list");
        }

        if(totalCredits+course.getCredits() <= 30){
            enrolledCourses.add(newCoursePair);
            totalCredits = totalCredits+course.getCredits();
        }
        else {
            throw new Exception_LimitECTS("The credits limit has been reached for " + this.getId(), this.getId());
        }

    }

    public void removeCourse(Course course) throws Exception_Input {
        if(enrolledCourses.stream().noneMatch(pair -> pair.getCourseId() == course.getId())){   //if it does not exist in the list
            throw new Exception_Input("Course does not exist in the student's list");
        }
        Pair searchedPair = enrolledCourses.stream()
                .filter(pair -> pair.getCourseId() == course.getId())
                .findAny()
                .orElse(null);

        //update the number of credits of the student
        totalCredits = totalCredits - course.getCredits();
        enrolledCourses.remove(searchedPair);
    }


    public void updateCredits(Course course, int newCredits) throws Exception_Input, Exception_LimitECTS{
        if(enrolledCourses.stream().noneMatch(pair -> pair.getCourseId() == course.getId())){
            throw new Exception_Input("Course does not exist in the student's list");
        }

        for(Pair actualCoursePair: enrolledCourses) {
            //update the nr of credits of the student -> 2 possibilities (remains under 30 de credits, or not)
            if (actualCoursePair.getCourseId() == course.getId()) {
                int newValue = totalCredits - actualCoursePair.getCredits() + newCredits;

                if (newValue <= 30) {
                    totalCredits = newValue;
                    break;
                } else {
                    //remove the course from the students list + throw to
                    enrolledCourses.remove(actualCoursePair);
                    totalCredits = totalCredits - actualCoursePair.getCredits();
                    throw new Exception_LimitECTS("The credits limit has been reached for " + this.studentId + ". Student unenrolled!", this.getId());
                }
            }
        }
    }


    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Pair> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Pair> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId()  +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", studentId=" + studentId +
                ", totalCredits=" + totalCredits +
                ", enrolledCourses=" + enrolledCourses +
                '}';
    }


    @Override
    public int compareTo(Student student2) {
        return this.getFirstName().compareTo(student2.getFirstName());
    }

    public boolean containsst(Student student) {
        return containsst(student);
    }
}
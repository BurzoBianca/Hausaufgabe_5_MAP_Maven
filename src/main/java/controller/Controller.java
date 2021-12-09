package controller;

import classes.Course;
import classes.Pair;
import classes.Student;
import classes.Teacher;
import repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private CourseRepo_JDBC courses;
    private StudentRepo_JDBC students;
    private TeacherRepo_JDBC teachers;

    public Controller(CourseRepo_JDBC courses, StudentRepo_JDBC students, TeacherRepo_JDBC teachers) {
        this.courses = courses;
        this.students = students;
        this.teachers = teachers;
    }

    public CourseRepo_JDBC getCourses() {
        return courses;
    }

    public StudentRepo_JDBC getStudents() {
        return students;
    }

    public TeacherRepo_JDBC getTeachers() {
        return teachers;
    }


    public boolean register(Course course, Student student) throws Exception_Input, Exception_LimitECTS, SQLException {
        if(students.getAll().stream().noneMatch(elem -> elem.getId() == student.getId())){
            throw new Exception_Input("No such student");
        }
        else
        if(courses.getAll().stream().noneMatch(elem -> elem.getId() == course.getId())){
            throw new Exception_Input("No such course");
        }
        else {

            student.addCourse(course);
            students.update(student);


            course.addStudent(student.getId());
            courses.update(course);

            return true;
        }
    }


    public List<Course> retriveCoursesWithFreePlaces(){
        return courses.getAll().stream()
                .filter(elem -> elem.getMaxEnrollement()-elem.getStudentsEnrolledId().size() >0)
                .collect(Collectors.toList());
    }


    public List<Integer> retrieveStudentsEnrolledForACourse(Course course) throws Exception_Input {
        if(courses.getAll().stream().anyMatch(elem -> elem.getId() == course.getId())){
            return course.getStudentsEnrolledId();
        }
        else
            throw new Exception_Input("No such course");
    }


    public List<Course> getAllCourses(){
        return courses.getAll();
    }


    public List<Course> deleteCourse(Course course) throws Exception_Input, SQLException {

        if(courses.getAll().contains(course)){
            for (Student student: students.getAll()){       //sterg cursul din listele tuturor studentilor
                List<Integer> enrolledCoursesId = student.getEnrolledCourses().stream()
                        .map(Pair::getCourseId).collect(Collectors.toList());  //din pereche iau doar id-ul

                if(enrolledCoursesId.contains(course.getId())){
                    student.removeCourse(course); //update REPO studenti
                    students.update(student);
                }
            }

            //delete din REPO profi
            int teacherId = course.getIdTeacher();

            Teacher teacher = teachers.getAll().stream()
                    .filter(elem -> elem.getId() == teacherId)
                    .findAny()
                    .orElse(null);
            assert teacher != null;
            teacher.removeCourse(course.getId());
            teachers.update(teacher);

            //delete din REPO cursuri
            courses.delete(course);


            return courses.getAll();
        }
        else {
            throw new Exception_Input("No such course");
        }
    }


    public List<Course> updateCreditsCourse(Course course, int newCredits) throws Exception_Input, IOException, SQLException {

        List<Integer> unenrollStudents = new ArrayList<>();

        if(courses.getAll().contains(course)) {
            //update REPO studenti
            for (int studentId : course.getStudentsEnrolledId()) {
                Student student = students.getAll().stream()
                        .filter(elem -> elem.getId() == studentId)
                        .findAny()
                        .orElse(null);
                try{

                    assert student != null;
                    if(student.getEnrolledCourses().stream().anyMatch(elem -> elem.getCourseId() == course.getId())) {
                        student.updateCredits(course, newCredits);
                        students.update(student);
                    }
                }
                catch (Exception_LimitECTS e){
                    System.out.println("Credit limit exceded for a student:" + e);
                    int problemStudentId = e.getId();
                    unenrollStudents.add(problemStudentId);
                }
            }
            if(unenrollStudents.size()>0){
                course.getStudentsEnrolledId().removeAll(unenrollStudents);
                courses.update(course);
            }

            course.setCredits(newCredits);
            courses.updateCredits(course);

            return courses.getAll();
        }
        else{
            throw new Exception_Input("No such course");
        }
    }

    public  List<Student> sortStudents(){
        List<Student> studentsList = students.getAll();
        Collections.sort(studentsList);
        return studentsList;
    }


    public List <Course> sortCourses(){
        List<Course> courseList = courses.getAll();
        Collections.sort(courseList);
        return courseList;
    }

    public List<Student> filterStudents(){
        return students.getAll().stream()
                .filter(entry -> entry.getFirstName().startsWith("D"))
                .collect(Collectors.toList());
    }


    public List<Course> filterCourses(){
        return courses.getAll().stream()
                .filter(entry -> entry.getCredits() > 10 )
                .collect(Collectors.toList());
    }

}
package repository;

//import com.mysql.cj.conf.ConnectionUrlParser;
import classes.Course;
import classes.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentRepo_JDBC extends JDBCRepo<Student> {

    public StudentRepo_JDBC(Statement stmt) throws SQLException {
        super(stmt);
    }


    @Override
    public List<Student> read() throws SQLException {
        String selectSql = "SELECT * FROM student left join enrolled_students es " +
                "on student.id = es.studentId " +
                "left join course c " +
                "on es.courseId = c.id";
        try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("student.id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                long studentId = resultSet.getLong("studentId");
                int totalCredits = resultSet.getInt("totalECTS");
                int courseId = resultSet.getInt("courseId");
                String name = resultSet.getString("name");
                int idTeacher = resultSet.getInt("teacherId");
                int maxEnrollment = resultSet.getInt("maxEnrollement");
                int credits = resultSet.getInt("credits");

                if (students.stream().anyMatch(student -> student.getId() == id)) {
                    Student searchedStudent = students.stream()
                            .filter(student -> student.getId() == id)
                            .findAny()
                            .orElse(null);
                    Course newCourse = new Course(courseId, name, idTeacher, maxEnrollment, credits);
                    assert searchedStudent != null;
                    searchedStudent.addCourse(newCourse);

                }
                else{
                    Student student = new Student(id, firstName, lastName, totalCredits);
                    if(courseId != 0){
                        Course newCourse = new Course(courseId, name, idTeacher, maxEnrollment, credits);
                        student.addCourse(newCourse);
                    }
                    students.add(student);
                }
            }
            repoList = students;
        }
        return repoList;
    }


    @Override
    public Student create(Student obj) throws SQLException {
        repoList.add(obj);
        stmt.executeUpdate("INSERT INTO student VALUES ("+ obj.getId()
                + ", \'" + obj.getFirstName() + "\', \'" + obj.getLastName() + "\' ," + obj.getStudentId()+ ", DEFAULT);");
        return obj;
    }


    @Override
    public Student update(Student obj) throws SQLException {

        stmt.executeUpdate("UPDATE student SET totalECTS = "+ obj.getTotalCredits() + " where id = " + obj.getId() +";");
        return obj;
    }


    @Override
    public void delete(Student obj) throws SQLException {
        repoList.remove(obj);
        stmt.executeUpdate("DELETE FROM student where id = "+ obj.getId()+";");
    }
}
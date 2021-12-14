package repository;

//import com.mysql.cj.conf.ConnectionUrlParser;
import classes.Course;
import classes.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentRepo_JDBC extends JDBCRepo<Student> {

    public StudentRepo_JDBC(Connection connection) throws SQLException {
        super(connection);
    }


    @Override
    public List<Student> read() throws SQLException {
        try( Statement stmt = connection.createStatement()){
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

                    } else {
                        Student student = new Student(id, firstName, lastName, totalCredits);
                        if (courseId != 0) {
                            Course newCourse = new Course(courseId, name, idTeacher, maxEnrollment, credits);
                            student.addCourse(newCourse);
                        }
                        students.add(student);
                    }
                }
                repoList = students;
            }catch (SQLException exception){
                exception.printStackTrace();
            }
        }
        return repoList;
    }


    @Override
    public Student create(Student obj) throws SQLException {
        try( Statement stmt = connection.createStatement()) {
            repoList.add(obj);
            stmt.executeUpdate("INSERT INTO student VALUES (" + obj.getId()
                    + ", \'" + obj.getFirstName() + "\', \'" + obj.getLastName() + "\' ," + obj.getStudentId() + ", DEFAULT);");
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        return obj;
    }


    @Override
    public Student update(Student obj) throws SQLException {
        try( Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("UPDATE student SET totalECTS = " + obj.getTotalCredits() + " where id = " + obj.getId() + ";");
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        return obj;
    }


    @Override
    public void delete(Student obj) throws SQLException {
        try( Statement stmt = connection.createStatement()) {
            repoList.remove(obj);
            stmt.executeUpdate("DELETE FROM student where id = " + obj.getId() + ";");
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }
}
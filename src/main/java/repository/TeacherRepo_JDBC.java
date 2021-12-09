package repository;

import classes.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepo_JDBC extends JDBCRepo<Teacher>{
    public TeacherRepo_JDBC(Statement stmt) throws SQLException {
        super(stmt);
    }

    @Override
    public List<Teacher> read() throws SQLException {
        String selectSql = "SELECT * FROM teacher left join course c on teacher.id = c.teacherId";

        try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
            List<Teacher> teachers = new ArrayList<>();
            while (resultSet.next()) {
                int id= resultSet.getInt("teacher.id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                int courseId = resultSet.getInt("c.id");

                if(courseId>0) {
                    if (teachers.stream().anyMatch(teacher -> teacher.getId() == id)) {
                        Teacher searchedTeacher = teachers.stream()
                                .filter(teacher -> teacher.getId() == id)
                                .findAny()
                                .orElse(null);
                        assert searchedTeacher != null;
                        searchedTeacher.addCourse(courseId);
                    } else {
                        Teacher teacher = new Teacher(id, firstName, lastName);
                        teacher.addCourse(courseId);
                        teachers.add(teacher);
                    }
                }
                else{
                    Teacher teacher = new Teacher(id, firstName, lastName);
                    teachers.add(teacher);
                }
            }
            repoList = teachers;
        }
        return repoList;

    }


    @Override
    public Teacher create(Teacher obj) throws SQLException {
        repoList.add(obj);
        stmt.executeUpdate("INSERT INTO teacher VALUES ("+ obj.getId()
                + ", \'" + obj.getFirstName() + "\', \'" + obj.getLastName() + "\');");
        return obj;
    }


    @Override
    public Teacher update(Teacher obj) {
        return obj;
    }


    @Override
    public void delete(Teacher obj) throws SQLException {
        repoList.remove(obj);
        stmt.executeUpdate("DELETE FROM teacher where id = "+ obj.getId()+";");
    }
}
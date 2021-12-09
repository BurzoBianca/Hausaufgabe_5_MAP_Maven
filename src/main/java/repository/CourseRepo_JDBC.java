package repository;

import classes.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseRepo_JDBC extends JDBCRepo<Course>{
    public CourseRepo_JDBC(Statement stmt) throws SQLException {
        super(stmt);
    }

    @Override
    public List<Course> read() throws SQLException {
        String selectSql = "SELECT * FROM course left join enrolled_students es " +
                "on course.id = es.courseId ";
        try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("course.id");
                String name = resultSet.getString("name");
                int idTeacher= resultSet.getInt("teacherId");
                int maxEnrollment = resultSet.getInt("maxEnrollement");
                int credits= resultSet.getInt("credits");
                int studentId = resultSet.getInt("studentId");

                if (courses.stream().anyMatch(course -> course.getId() == id)) {
                    Course searchedcourse = courses.stream()
                            .filter(course -> course.getId() == id)
                            .findAny()
                            .orElse(null);
                    assert searchedcourse != null;
                    searchedcourse.addStudent(studentId);
                }
                else{
                    Course course = new Course(id, name, idTeacher,maxEnrollment,credits);
                    if(studentId != 0){
                        course.addStudent(studentId);
                    }
                    courses.add(course);
                }
            }
            repoList = courses;
        }
        return repoList;

    }


    @Override
    public Course create(Course obj) throws SQLException {
        repoList.add(obj);
        stmt.executeUpdate("INSERT INTO course VALUES ("+ obj.getId()
                + ", \'" + obj.getName() + "\', " + obj.getIdTeacher() + "," + obj.getMaxEnrollement()+ ", " + obj.getCredits() + ");");
        return obj;
    }


    @Override
    public Course update(Course obj) throws SQLException {
        List<Integer> oldStudents = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT studentId FROM enrolled_students  WHERE courseId = " + obj.getId() +";" );
        while(rs.next()){
            oldStudents.add(rs.getInt("studentId"));
        }

        if(oldStudents.size() > obj.getStudentsEnrolledId().size()){   //cand un student a fost sters
            List<Integer> aux = new ArrayList<>(oldStudents);
            aux.removeAll(obj.getStudentsEnrolledId());

            for (int deletedStudentId : aux) {
                oldStudents.remove(deletedStudentId);

                stmt.executeUpdate("DELETE FROM enrolled_students WHERE courseId = " + obj.getId() + " AND studentId = " + deletedStudentId + ";");            }

        }
        else
        if(oldStudents.size() < obj.getStudentsEnrolledId().size()) {    //cand un student a fost adaugat
            List<Integer> aux = new ArrayList(obj.getStudentsEnrolledId());
            aux.removeAll(oldStudents);
            int addedStudentId = aux.get(0);
            oldStudents.add(addedStudentId);

            stmt.executeUpdate("INSERT INTO enrolled_students VALUES(" + addedStudentId + ", " + obj.getId() + ");");
        }

        return obj;
    }


    public Course updateCredits(Course obj) throws SQLException {
        stmt.executeUpdate("UPDATE course SET credits = "+ obj.getCredits() + " where id = " + obj.getId() +";");

        return obj;
    }


    @Override
    public void delete(Course obj) throws SQLException {
        repoList.remove(obj);

        stmt.executeUpdate("DELETE FROM enrolled_students WHERE courseId = " + obj.getId()+ ";");
        stmt.executeUpdate("DELETE FROM course where id = "+ obj.getId()+";");

    }
}
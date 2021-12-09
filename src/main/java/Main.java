import controller.*;
import classes.*;
import repository.*;

import java.io.IOException;
import java.sql.*;

public class Main {

    static final String DB_URL = "jdbc:mysql://localhost:3306/hausaufgabe_5_map?enabledTLSProtocols=TLSv1.2";
    static final String USER = "root";
    static final String PASS = "Biancaare3pene!";

    public static void main(String[] args) throws IOException, SQLException, Exception_Input {

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
 /*
        Statement stmt = connection.createStatement();
        TeacherRepo_JDBC teacherRepo_jdbc = new TeacherRepo_JDBC(stmt);
        StudentRepo_JDBC studentRepo_jdbc = new StudentRepo_JDBC(stmt);
        CourseRepo_JDBC courseRepo_jdbc = new CourseRepo_JDBC(stmt);

        System.out.println(teacherRepo_jdbc.getAll());
        System.out.println(studentRepo_jdbc.getAll());
        System.out.println(courseRepo_jdbc.getAll());


        Controller controller = new Controller(courseRepo_jdbc,studentRepo_jdbc,teacherRepo_jdbc);

        System.out.println(controller.retriveCoursesWithFreePlaces());
        System.out.println(controller.getAllCourses());
        System.out.println(controller.sortCourses());
        System.out.println(controller.filterCourses());

        //register
        Course course1 = new Course(1,"MAP",11,2, 10 );
        Course course2 = new Course(2, "BD", 11, 2, 10);
        Course course3  = new Course(3,"LP", 12, 2, 11);

        Student student1 = new Student(1,"Catalina", "Vasiu", 110);
        Student student2 = new Student(2,"Victor", "Santa", 125);
        Student student3 = new Student(3,"Darius", "Oros", 118);

        controller.register(course1,student1);
        controller.register(course1,student2);
        controller.register(course2,student1);
        controller.register(course2,student3);
        controller.register(course3,student3);

        System.out.println(studentRepo_jdbc.getAll());
        System.out.println(courseRepo_jdbc.getAll());


        Conexiune con = new Conexiune();
        Connection conn = con.connection();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from course");

        while(rs.next()){

            System.out.println(rs.getInt("id"));

        }*/
    }

}

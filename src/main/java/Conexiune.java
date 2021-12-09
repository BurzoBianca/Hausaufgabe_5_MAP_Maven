import controller.*;
import classes.*;
import repository.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexiune {

    static final String DB_URL = "jdbc:mysql://localhost:3306/hausaufgabe_5_map";
    static final String USER = "root";
    static final String PASS = "Biancaare3pene!";

    public Connection connection(){

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

}

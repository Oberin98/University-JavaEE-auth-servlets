package db;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionServlet extends HttpServlet {
    protected Connection connection = null;

    public void init() {
        String url = "jdbc:mysql://localhost/java_auth";
        String user = "root";
        String password = "";

        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception exception) {
            System.out.println(exception.toString() + "DB CONNECTION ERROR");
        }
    }

    protected String toSqlString(String value) {
        return "\"" + value + "\"";
    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

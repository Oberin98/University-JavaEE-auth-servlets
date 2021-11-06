package server;

import db.ConnectionServlet;
import helpers.AuthCookieHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "Login", value = "/login")
public class Login extends ConnectionServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            AuthCookieHelper authCookieHelper = new AuthCookieHelper(cookies);

            if (!authCookieHelper.isEmpty()) {
                response.sendRedirect("/account");
            } else {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }


        } catch (Exception exception) {
            request.getRequestDispatcher("/404.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");
        String login = request.getParameter("login");

        String userQuery = new StringBuilder()
                .append("SELECT DISTINCT * FROM Users WHERE login = ")
                .append(toSqlString(login))
                .append(" AND password = ")
                .append(toSqlString(password))
                .toString();

        try {
            Statement statement = connection.createStatement();
            ResultSet userResult = statement.executeQuery(userQuery);

            if (userResult.next()) {
                String userId = userResult.getString("id");
                Cookie userIdCookie = new Cookie("userId", userId);
                Cookie loginCookie = new Cookie("login", login);

                response.addCookie(userIdCookie);
                response.addCookie(loginCookie);
                response.sendRedirect("/account");

            } else {
                throw new Exception("Not found");
            }
        } catch (Exception exception) {
            request.getRequestDispatcher("/404.jsp").forward(request, response);
        }
    }
}

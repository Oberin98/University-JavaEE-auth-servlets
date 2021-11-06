package server;

import db.ConnectionServlet;
import helpers.AuthCookieHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

@WebServlet(name = "Account", value = "/account")
public class Account extends ConnectionServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        AuthCookieHelper authCookieHelper = new AuthCookieHelper(cookies);

        if (!authCookieHelper.isEmpty()) {
            String userId = authCookieHelper.getUserId();
            String login = authCookieHelper.getLogin();

            request.setAttribute("userId", userId);
            request.setAttribute("login", login);

            doPost(request, response);
        } else {
            request.getRequestDispatcher("/forbidden.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isLogout = Objects.equals(request.getParameter("logout"), "true");

        if (isLogout) {
            Cookie userIdCookie = new Cookie("userId", "");
            Cookie loginCookie = new Cookie("login", "");
            userIdCookie.setMaxAge(0);
            loginCookie.setMaxAge(0);
            response.addCookie(userIdCookie);
            response.addCookie(loginCookie);

            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } else {
            try {
                Cookie[] cookies = request.getCookies();
                AuthCookieHelper authCookieHelper = new AuthCookieHelper(cookies);
                String userId = authCookieHelper.getUserId();
                String login = authCookieHelper.getLogin();
                String subjectId = request.getParameter("id");

                Statement statement = connection.createStatement();

                if (subjectId != null) {
                    String updateQuery = new StringBuilder()
                            .append("UPDATE Rating SET checked = true WHERE id = ")
                            .append(subjectId)
                            .append(" AND userId = ")
                            .append(userId)
                            .toString();

                    statement.executeUpdate(updateQuery);
                }

                String ratingQuery = new StringBuilder()
                        .append("SELECT * FROM Rating WHERE userId = ")
                        .append(toSqlString(userId))
                        .toString();

                ResultSet result = statement.executeQuery(ratingQuery);
                String rating = generateAccountRatingMarkup(result);

                request.getSession().setAttribute("rating", rating);
                request.getSession().setAttribute("login", login);
                request.getRequestDispatcher("/account.jsp").forward(request, response);

            } catch (Exception exception) {
                System.out.println(exception);
                request.getRequestDispatcher("/404.jsp").forward(request, response);
            }

        }
    }

    private String generateAccountRatingMarkup(ResultSet result) {
        String ratingMarkup = "";

        try {
            while (result.next()) {
                ratingMarkup += "<form style='margin-bottom: 10px;'>";
                ratingMarkup += "<span style='padding-right: 10px;'>" + result.getString("setDate") + "</span>";
                ratingMarkup += "<span style='padding-right: 10px;'>" + result.getString("subject") + "</span>";
                ratingMarkup += "<span style='padding-right: 10px;'>" + result.getString("rating") + "</span>";

                if (Integer.parseInt(result.getString("checked")) == 0) {
                    ratingMarkup += "<input hidden name='id' value='" + result.getString("id") + "' />";
                    ratingMarkup += "<button type='submit'>Approve</button>";
                }

                ratingMarkup += "</form>";
            }
            return ratingMarkup;
        } catch (Exception exception) {
            return ratingMarkup;
        }
    }
}

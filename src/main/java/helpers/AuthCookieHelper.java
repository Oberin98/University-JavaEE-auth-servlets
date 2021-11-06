package helpers;

import javax.servlet.http.Cookie;
import java.util.Objects;

public class AuthCookieHelper {
    private String login = "";
    private String userId = "";

    public AuthCookieHelper(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "userId")) userId = cookie.getValue();
            if (Objects.equals(cookie.getName(), "login")) login = cookie.getValue();
        }
    }

    public String getLogin() {
        return login;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean isEmpty() {
        return login.length() == 0 && userId.length() == 0;
    }
}

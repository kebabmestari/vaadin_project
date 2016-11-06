package web;

import com.user.User;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Instance of user session in application
 * Created by samlinz on 31.10.2016.
 */
public class UserSession {

    // constant for idle timeout in minutes
    public static int TIMEOUT_MINUTES = 10;

    private Date loginTime;
    private User user;

    private boolean loggedIn;

    private UserSession() {
        loginTime = new Date();
        loggedIn = false;
    }

    public void loginUser() {
        loggedIn = true;
        loginTime = new Date();
        LOG.info("Logged in user " + user.getName());
    }

    public void logoutUser() {
        loggedIn = false;
        LOG.info("Logged out user " + user.getName());
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public static UserSession getSession(User user) {
        UserSession session = new UserSession();
        session.setUser(user);
        return session;
    }

    private static Logger LOG = Logger.getLogger(UserSession.class.getName());
}

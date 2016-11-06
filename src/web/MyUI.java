package web;

import com.database.DatabaseConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Application entry point
 */

/*@Theme("theme")*/
public class MyUI extends UI {

    // user session if logged in
    UserSession user;

    // database connector object
    public static DatabaseConnector dbConn;

    // user ip
    private String ip;

    // content provider
    // serves all contents in the application
    private ContentProvider provider;

    /**
     * Initialize Vaadin application
     *
     * @param vaadinRequest request object
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        // get address
        ip = vaadinRequest.getRemoteAddr();
        // get content provider
        provider = ContentProvider.getProvider();

        // direct user to login if the user is not logged in
        if (user != null) {
            if (user.isLoggedIn()) {
                provider.getMainPage();
            }
        } else {
            provider.getLoginPage();
        }
    }

    // return session
    public UserSession getUser() {
        return user;
    }

    // set session
    public void setUser(UserSession user) {
        this.user = user;
    }

    static {
        dbConn = DatabaseConnector.getInstance();
        dbConn.connect("localhost", 3306, "webUser", "kebab");
    }

}

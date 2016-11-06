package web;

import com.user.User;
import com.user.UserProvider;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;

import java.util.logging.Logger;


public class LoginPage extends VerticalLayout {

    protected TextField userNameInput;
    protected PasswordField passwordInput;
    protected Button loginButton;
    protected Button signupButton;

    private ContentProvider provider;

    public void init(ContentProvider provider) {

        this.provider = provider;

        userNameInput = new TextField();
        passwordInput = new PasswordField();
        loginButton = new Button("Login");
        signupButton = new Button("Sign Up");

        // User name and password inputs
        userNameInput.setImmediate(true);
        userNameInput.setInputPrompt("Username");

        passwordInput.setImmediate(true);
        passwordInput.setInputPrompt("Password");

        // Add input text fields to layout
        HorizontalLayout textfields = new HorizontalLayout();
        textfields.addComponents(userNameInput, passwordInput);
        textfields.setSpacing(true);
        this.addComponent(textfields);


        // Login and sign up buttons
        loginButton.addClickListener(e -> {
            String foo = userNameInput.getValue();
            String bar = passwordInput.getValue();
            if (UserProvider.authenticateUser(foo, bar)) {
                LOG.info("Logged in as " + foo);
                // fetch the user data from db
                User loggedUser = UserProvider.getUser(foo);
                // show notification
                Notification.show("Login success", Notification.Type.ASSISTIVE_NOTIFICATION);
                // start a new session for the user
                ((MyUI) UI.getCurrent()).setUser(UserSession.getSession(loggedUser));
                provider.getMainPage();
            } else {
                LOG.info("Authenticating user " + foo + " failed!");
                loginButton.setComponentError(new UserError("Invalid credentials"));
            }
        });

        // open sign up form
        signupButton.addClickListener(e -> {
            LOG.info("Fetching sign-up window");
            provider.openSignUp();
        });

        // Add buttons to LoginPage layout
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(loginButton, signupButton);
        buttons.setSpacing(true);

        this.addComponent(buttons);

        // Layout
        this.setWidth("30%");
        this.setMargin(true);
        this.setSpacing(true);
    }

    protected VerticalLayout buildUserPage(String username, String password) {
        //connector.checkLogin(username);
        /*
        if (username.equals("admin") && password.equals("admin")) {
			AdminPage adminPage = new AdminPage();
        	adminPage.initAdminPage();
			return adminPage;
		} else {
			ProfilePage profilePage = new ProfilePage();
			profilePage.initChart();
			return profilePage;
		}*/
        return null;
    }

    private Logger LOG = Logger.getLogger(LoginPage.class.getName());

}

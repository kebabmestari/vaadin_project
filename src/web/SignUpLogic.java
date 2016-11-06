package web;

import com.database.StringCrypt;
import com.user.User;
import com.user.UserFactory;
import com.user.UserProvider;
import com.vaadin.server.UserError;
import com.vaadin.ui.Notification;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by samlinz on 2.11.2016.
 */
class SignUpLogic extends SignUpForm {
    public SignUpLogic() {
        super();
        signUpButton.addClickListener((e) -> {
            String name = usernameInput.getValue();
            String pwd = passwordInput.getValue();
            if (name.length() == 0 || pwd.length() == 0) {
                signUpButton.setComponentError(new UserError("Fill the both fields"));
            } else {
                LOG.info("Trying to create user " + name);
                createNewUser(name, pwd);
            }
        });
    }

    /**
     * Create a new user with the input credentials
     *
     * @param name
     * @param password
     */
    public void createNewUser(String name, String pwd) {
        try {
            if (UserProvider.userExists(name)) {
                signUpButton.setComponentError(new UserError("Username exists already"));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        LOG.info("User " + name + " doesn't exists, creating");
        pwd = StringCrypt.encrypt(pwd);
        User newUser = UserFactory.createUser(name, UserProvider.getNextId(), new Date());
        if (UserFactory.flushUser(newUser, pwd)) {
            User getNewUser = UserProvider.getUser(name);
            if (getNewUser != null && getNewUser.getId() == newUser.getId()) {
                LOG.info("Creating user " + name + " succeeded");
                Notification.show("User creation succeeded", "You can now log in", Notification.Type.HUMANIZED_MESSAGE);
                return;
            } else {
                LOG.warning("Creating user " + name + " failed");
            }
        }
        Notification.show("Creating user " + name + " failed", Notification.Type.ERROR_MESSAGE);
    }

    private static Logger LOG = Logger.getLogger(SignUpLogic.class.getName());
}

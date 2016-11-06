package com.user;

import com.database.Query;
import com.database.QueryRunner;
import com.database.StringCrypt;
import com.util.DateProvider;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import static com.user.UserProvider.userExists;

/**
 * Responsible for creating users
 * and fetching existsing ones from db
 * Created by samlinz on 26.10.2016.
 */
public class UserFactory {

    /**
     * Create new user object
     *
     * @param name user name
     * @param id
     * @param date
     * @return user object
     */
    public static User createUser(String name, int id, Date date) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setId(id);
        newUser.setCreationDate(date);
        return newUser;
    }

    /**
     * Create a completely new user and flush it straight way into database
     *
     * @param name name
     * @param pwd  password
     */
    public static boolean createNewUserAndFlushToDatabase(String name, String pwd)
            throws UserExistsAlreadyException {
        name = name.toLowerCase();

        // Should this happen in frontend instead? Possible security hazard
        // if we are sending plaintext pwds in cookies
        // FIXME: 26.10.2016
        pwd = StringCrypt.encrypt(pwd);

        try {
            if (userExists(name)) {
                throw new UserExistsAlreadyException(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        User newUser = createUser(name, UserProvider.getNextId(), DateProvider.getDate());
        if (flushUser(newUser, pwd)) {
            LOG.info("New user " + name + " successfully created and flushed to database");
            UserProvider.addUserToUserMap(newUser);
            return true;
        } else {
            LOG.warning("Failed to flush ");
        }
        return false;
    }

    /**
     * Flush new user to database
     *
     * @param user user object
     */
    public static boolean flushUser(User user, String pwd) {
        LOG.info("Flushing user " + user.getId() + "-" + user.getName() + " to database");
        QueryRunner qr = QueryRunner.getRunner(Query.CREATE_USER);
        qr.setParam(user.getName());
        qr.setParam(pwd);
        boolean result = qr.runUpdate() > 0;
        return result;
    }

    // logger
    private static Logger LOG = Logger.getLogger(UserFactory.class.getName());
}

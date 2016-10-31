package com.user;

import com.database.Queries;
import com.database.Query;
import com.database.StringCrypt;
import com.util.DateProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            UserProvider.addUserToUserMap(name, newUser);
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
        PreparedStatement st = Queries.getQuery(Query.CREATE_USER);
        try {
            st.setString(1, user.getName());
            st.setString(2, StringCrypt.encrypt(pwd));
            if (st.executeUpdate() > 0) {
                LOG.info("User " + user + " flushed to database");
                return true;
            } else {
                LOG.warning("Flushing user " + user + " to database failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // logger
    private static Logger LOG = Logger.getLogger(UserFactory.class.getName());
}

package com.user;

import com.database.Queries;
import com.database.Query;
import com.database.StringCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static com.user.UserProvider.userExists;

/**
 * Responsible for creating users
 * and fetching existsing ones from db
 * Created by samlinz on 26.10.2016.
 */
public class UserFactory {
    /**
     * Create a new user and push it to the db
     *
     * @param name
     * @param pwd
     * @return
     */
    public static boolean fetchUsers(String name, String pwd) {
        name = name.toLowerCase();
        try {
            if (!userExists(name)) {
                PreparedStatement st = Queries.getQuery(Query.CREATE_USER);
                st.setString(1, name);
                st.setString(1, StringCrypt.encrypt(pwd));

                ResultSet res = st.executeQuery();
                res.next();

                User newUser = createUser(res.getString("name"), res.getInt("idUser"));
                UserProvider.addUserToUserMap(name, newUser);
            } else {
                LOG.warning("Cannot create user " + name + ". User exits already.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warning("Error occurred in user creation. User " + name + " was not created.");
        }
        return false;
    }

    /**
     * Create new user object
     *
     * @param name user name
     * @return user object
     */
    public static User createUser(String name, int id) {
        User newUser = new User();
        newUser.setName(name);
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
        User newUser = createUser(name, UserProvider.getNextId());
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

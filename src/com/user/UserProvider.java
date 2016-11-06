package com.user;

import com.database.*;

import com.util.DateProvider;
import org.eclipse.jetty.util.log.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Fetch user data from database and methods to fetch user info and objects
 * Created by samlinz on 17.10.2016.
 */
public class UserProvider {

    // logger
    private static Logger LOG = Logger.getLogger(UserProvider.class.getName());

    // map user objects to names to optimize
    private static Map<String, User> userMap = new HashMap<>();

    /**
     * Fetch a user data from db and create an object
     *
     * @param identifier
     * @return
     */
    public static <T> boolean fetchUser(T identifier) {
        try {
            if (userExists(identifier)) {

                QueryRunner qr = QueryRunner.getRunner(Query.GET_USER_INFO);
                // accept different types of indentifiers
                if (identifier instanceof String) {
                    qr.setParam((String) identifier);
                    qr.setParam(-1);
                } else if (identifier instanceof Integer) {
                    qr.setParam("");
                    qr.setParam((Integer) identifier);
                } else {
                    LOG.warning("Invalid type given to fetchuser");
                    return false;
                }

                qr.run();
                String name = qr.getFirstResult(String.class, "name");
                int id = qr.getFirstResult(Integer.class, "id");
                Date date = qr.getFirstResult(Date.class, "date");
                User newUser = UserFactory.createUser(name, id, date);
                UserProvider.addUserToUserMap(newUser);
                LOG.info("User " + name + " fetched from database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks whether user exists in database
     *
     * @param name user name
     * @return true if user exists, false otherwise
     * @throws SQLException sql exception if there was problem with connection rather that finding user
     */
    public static <T> boolean userExists(T identifier) throws SQLException {
        QueryRunner qr;
        if(identifier instanceof String){
            qr = QueryRunner.getRunner(Query.GET_USER_EXISTS_BY_NAME);
            qr.setParam((String) identifier);
        } else if(identifier instanceof Integer) {
            qr = QueryRunner.getRunner(Query.GET_USER_EXISTS_BY_ID);
            qr.setParam((Integer) identifier);
        } else {
            LOG.warning("Invalid user identifier");
            return false;
        }
        qr.run();
        final long users = qr.getResults(Long.class, "NUMBER").get(0);
        if(users == 1) {
            return true;
        } else if(users > 1) {
            LOG.severe("Duplicate user id's in db!");
            return true;
        }
        return false;
    }

    /**
     * Authenticate user
     *
     * @param name     user name
     * @param password user password
     * @return true if the pwd was correct, false otherwise
     */
    public static boolean authenticateUser(String name, String password) {
        LOG.info("Authenticating user " + name);
        QueryRunner qr = QueryRunner.getRunner(Query.GET_USER_INFO);
        // search by name, not id
        qr.setParam(name);
        qr.setParam(-1);
        qr.run();
        password = StringCrypt.encrypt(password);
        List<String> pwd2 = qr.getResults(String.class, "passwd");
        return pwd2.size() > 0 ? password.equals(pwd2.get(0)) : false;
    }

    /**
     * Goes through the users and finds the next id number
     *
     * @return highest existsing id incremented by one
     */
    public static int getNextId() {
        QueryRunner qr = QueryRunner.getRunner(Query.GET_MAX_USER_ID);
        qr.run();
        int num = 0;
        try {
            num = qr.getResults(Integer.class, "max").get(0);
        } catch (NullPointerException e) {
            num = 0;
        }
        return  num + 1;
    }

    /**
     * After user logs out etc
     * it's data and related objects are freed from memory
     */
    public static void freeUser(User user) {
        user.free();
        userMap.remove(user.getName());
    }

    /**
     * Return user from memory or load from db
     *
     * @param name user name
     * @return User object
     */
    public static User getUser(String name) {
        name = name.toLowerCase();
        User result = userMap.get(name);
        if (result == null) {
            try {
                if (userExists(name)) {
                    fetchUser(name);
                    result = userMap.get(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Return user by id
     *
     * @param id user id
     * @return User object
     */
    public static User getUser(int id) {
        User result = null;

        // iterate twice, if not found on first try to load from database
        // if not found then the user does not exists
        for (int i = 0; i < 2; i++) {
            for (Map.Entry e : userMap.entrySet()) {
                User u = (User) e.getValue();
                if (u.getId() == id) {
                    result = u;
                    break;
                }
            }
            if (result == null) {
                try {
                    if (userExists(id)) {
                        fetchUser(id);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Map user object to user hashmap
     *
     * @param name user name
     * @param user user object
     */
    static void addUserToUserMap(User user) {
        userMap.put(user.getName(), user);
    }
}

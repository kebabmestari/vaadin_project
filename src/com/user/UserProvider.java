package com.user;

import com.database.DatabaseConnector;
import com.database.Queries;
import com.database.Query;
import com.database.StringCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
     * Checks whether user exists in database
     *
     * @param name user name
     * @return true if user exists, false otherwise
     * @throws SQLException sql exception if there was problem with connection rather that finding user
     */
    public static <T> boolean userExists(T identifier) throws SQLException {
        LOG.fine("Checking whether user " + identifier.toString() + " exists");
        PreparedStatement st;
        if (identifier instanceof Integer) {
            st = Queries.getQuery(Query.GET_USER_EXISTS_BY_ID);
            st.setInt(1, (Integer) identifier);
        } else if ( identifier instanceof String) {
            st = Queries.getQuery(Query.GET_USER_EXISTS_BY_NAME);
            st.setString(1, DatabaseConnector.sanitizeString((String) identifier));
        }
        ResultSet res = st.executeQuery();
        if (res.next()) {
            return res.getInt(1) > 0;
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
        PreparedStatement st = Queries.getQuery(Query.GET_USER_INFO);
        try {
            st.setString(1, name);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                password = StringCrypt.encrypt(password);
                return password.equals(res.getString("passwd"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Goes through the users and finds the next id number
     *
     * @return highest existsing id incremented by one
     */
    public static int getNextId() {
        int id = 1;
        for (Map.Entry e : userMap.entrySet()) {
            int tempId = ((User) e.getValue()).getId();
            if (tempId > id) {
                id = tempId;
            }
        }
        return id;
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
            if (userExists(name)) {
                UserFactory.fetchUser(name);
                // recursive call, but this time the user exists in map
                return getUser(name);
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
        User result;
        for (Map.Entry e : userMap.entrySet()) {
            User u = (User) e.getValue();
            if (u.getId() == id) {
                result = u;
                break;
            }
        }
        if (result == null) {
            if (userExists(id)) {
                UserFactory.fetchUser(id);
                // recursive call, but this time the user exists in map
                return getUser(id);
            }
        }
    }

    /**
     * Map user object to user hashmap
     *
     * @param name user name
     * @param user user object
     */
    static void addUserToUserMap(String name, User user) {
        userMap.put(name, user);
    }
}

package com.user;

import com.database.*;
import com.util.DateProvider;

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
     * Fetch a user data from db and create an object
     *
     * @param identifier
     * @return
     */
    public static <T> boolean fetchUser(T identifier) {
        try {
            if (userExists(identifier)) {

                PreparedStatement st = Queries.getQuery(Query.GET_USER_INFO);

                // accept different types of indentifiers
                if(identifier instanceof String) {
                    st.setString(1, (String) identifier);
                    st.setInt(2, -1);
                } else if(identifier instanceof Integer) {
                    st.setString(1, "");
                    st.setInt(2, (Integer) identifier);
                } else {
                    LOG.warning("Invalid type given to fetchuser");
                    return false;
                }

                ResultSet res = st.executeQuery();
                if(res.next()) {
                    String tempName = res.getString("name");
                    User newUser = UserFactory.createUser(tempName, res.getInt("idUser"),
                            DateProvider.getDateAsDate(res.getString("date")));
                    UserProvider.addUserToUserMap(tempName, newUser);
                } else {
                    LOG.info("Could not fetch user " + identifier + ". User does not exist");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warning("Error occurred in user creation. User " + identifier + " was not created.");
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
        LOG.info("Checking whether user " + identifier.toString() + " exists");
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
        final int users = qr.getResultRows();
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
        QueryRunner qr = QueryRunner.getRunner(Query.GET_MAX_USER_ID);
        qr.run();
        return qr.getResults(Integer.class, "max").get(0) + 1;
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
                    // recursive call, but this time the user exists in map
                    return getUser(name);
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
                    // recursive call, but this time the user exists in map
                    result = getUser(id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
    static void addUserToUserMap(String name, User user) {
        userMap.put(name, user);
    }
}

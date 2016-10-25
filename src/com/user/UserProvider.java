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
    public static boolean userExists(String name) throws SQLException {
        LOG.fine("Checking whether user " + name + " exists");
        PreparedStatement st = Queries.getQuery(Query.GET_USER_EXISTS);
        st.setString(1, DatabaseConnector.sanitizeString(name));
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
            int tempId = ((User)e.getValue()).getId();
            if (tempId > id) {
                id = tempId;
            }
        }
        return id;
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

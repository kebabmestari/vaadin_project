package com.user;

import com.database.DatabaseConnector;
import com.database.Queries;
import com.database.Query;

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

    private static void fetchUserToMap(String name) {
//        Queries.getQuery("getUser");
    }

    /**
     * Checks whether user exists in database
     *
     * @param name user name
     * @return true if user exists, false otherwise
     * @throws SQLException sql exception if there was problem with connection rather that finding user
     */
    public static boolean userExists(String name) throws SQLException {
        PreparedStatement st = Queries.getQuery(Query.GET_USER_EXISTS);
        st.setString(1, DatabaseConnector.sanitizeString(name));
        ResultSet res = st.executeQuery();
        res.next();
        return res.getInt(1) > 0;
    }

    public static boolean authenticateUser(String name, String password) {

    }

    /**
     * Create a new user and push it to the db
     * @param name
     * @param pwd
     * @return
     */
    public static boolean createNewUser(String name, String pwd) {
        try {
            if(!userExists(name)) {
                PreparedStatement st = Queries.getQuery(Query.CREATE_USER);
                st.setString(1, name);
                st.setString(1, com.database.StringCrypt.encrypt(pwd));
                // FIXME: 25.10.2016 KÄYTTÄJÄOBJEKTIN LISÄYS
            } else {
                LOG.warning("Cannot create user " + name +". User exits already.");
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
     * Map user object to user hashmap
     * @param name user name
     * @param user user object
     */
    private static void addUserToMap(String name, User user) {
        userMap.put(name, user);
    }
}

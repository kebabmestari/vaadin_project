package com.user;

import com.database.DatabaseConnector;
import com.database.Queries;
import com.game.Result;
import com.word.WordList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class representing the user of database
 * Created by samlinz on 17.10.2016.
 */
public class User {

    private static Logger LOG;

    // user account name
    private String name;
    // list of user's lists
    private Set<WordList> ownLists;
    // user favourited lists
    private Set<WordList> favourites;

    // instatiate using the provider
    public User(String name) {
        this.name = name;
        LOG.fine("User object " + name + " created.");
    }

    /**
     * Fetch all user results
     */
    public List<Result> getResults() {
        // FIXME: 17.10.2016 TODO
        return null;
    }

    /**
     * Fetch user results from a timespan
     */
    public List<Result> getResults(Date start, Date end) {
        if (start.compareTo(end) <= 0) {
            LOG.warning("Invalid dates");
            return null;
        }
        // FIXME: 17.10.2016 TODO
        return null;
    }

    /**
     * Checks whether user exists in database
     *
     * @param name user name
     * @return true if user exists, false otherwise
     * @throws SQLException
     */
    public static boolean userExists(String name) throws SQLException {
        PreparedStatement st = Queries.getQuery("getUserExists");
        st.setString(1, DatabaseConnector.sanitizeString(name));
        ResultSet res = st.executeQuery();
        res.next();
        return res.getInt(1) > 0;
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
                PreparedStatement st = Queries.getQuery("registerNewUser");
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

    static {
        LOG = Logger.getLogger(User.class.getName());
    }
}

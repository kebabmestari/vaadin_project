package com.user;

import com.database.DatabaseConnector;
import com.database.Queries;
import com.game.Result;
import com.word.WordList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Class representing the user of database
 * Created by samlinz on 17.10.2016.
 */
public class User {

    // logger
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

    static {
        LOG = Logger.getLogger(User.class.getName());
    }

}

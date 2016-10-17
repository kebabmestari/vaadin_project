package com.user;

import com.game.Result;
import com.word.WordList;

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
    private User() {}

    /**
     * Fetch all user results
     */
    public List<Result> getResults() {
        // FIXME: 17.10.2016 TODO
    }

    /**
     * Fetch user results from a timespan
     */
    public List<Result> getResults(Date start, Date end) {
        if (start.compareTo(end) <= 0) {
            LOG.warning("Invalid dates");
            return null;
        }
        // FIXME: 17.10.2016 TODO
    }

    static {
        LOG = Logger.getLogger(User.class.getName());
    }
}

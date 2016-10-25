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

    // user account name
    private String name;
    // identifier
    private int id;

    // list of user's lists
    private Set<WordList> ownLists;

    // results
    private List<Result> results;

    // user favourited lists
    private Set<WordList> favourites;

    // instatiate using the provider
    User() {
        LOG.fine("User object " + this.toString() + " created.");
    }

    /**
     * Fetch all user results
     */
    public List<Result> getResults() {
        return results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Add user result to list
     * @param res result object
     */
    public void addResult(Result res) {
        results.add(res);
    }


    /**
     * Fetch user results from a timespan
     */
/*    public List<Result> getResults(Date start, Date end) {
        if (start.compareTo(end) <= 0) {
            LOG.warning("Invalid dates");
            return null;
        }
        // FIXME: 17.10.2016 TODO
        return null;
    }*/

    private static final Logger LOG = Logger.getLogger(User.class.getName());

}

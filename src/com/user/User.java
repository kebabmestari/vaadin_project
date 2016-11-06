package com.user;

import com.database.Freeable;
import com.game.Result;
import com.word.WordList;

import java.util.*;
import java.util.logging.Logger;

/**
 * Class representing the user of database
 * Created by samlinz on 17.10.2016.
 */
public class User implements Freeable {

    // user account name
    private String name;
    // identifier
    private int id;
    // creation date
    private Date creationDate;

    // list of user's lists
    private Set<WordList> ownLists = new HashSet<>();

    // results
    private List<Result> results = new ArrayList<>();

    // user favourited lists
    private Set<WordList> favourites = new HashSet<>();

    // instatiate using the provider
    User() {
        LOG.info("User object " + this.toString() + " created.");
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Add user result to list
     *
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

    /**
     * Free user from memory
     * so it can be garbage collected
     */
    @Override
    public void free() {
        LOG.info("Freeing user " + getName() + " memory");
        this.results = null;
        this.ownLists = null;
        this.favourites = null;
    }

    private static final Logger LOG = Logger.getLogger(User.class.getName());

}

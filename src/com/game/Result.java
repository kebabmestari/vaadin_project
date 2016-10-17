package com.game;

import com.user.User;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Object represents a result from a single (completed) run
 * Created by samlinz on 17.10.2016.
 */
public class Result {
    // result data
    private Date date;

    // time in seconds
    private int time;
    // run score
    private short score;
    // max score
    private short maxScore;
    // reference to the user who made the result
    private User user;


    // logger
    private static Logger LOG;

    static {
        LOG = Logger.getLogger(Result.class.getName());
    }
}

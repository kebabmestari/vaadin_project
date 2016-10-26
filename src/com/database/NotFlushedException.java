package com.database;

/**
 * Created by samlinz on 27.10.2016.
 */
public class NotFlushedException extends Exception {
    public NotFlushedException() {
        super("Cannot rebuild database status, there are new objects not flushed to database");
    }
}

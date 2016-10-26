package com.database;

/**
 * Tags object as freeable
 * in other words it can be freed from memory if it's
 * not used anymore
 * Created by samlinz on 26.10.2016.
 */
public interface Freeable {
    void free();
}
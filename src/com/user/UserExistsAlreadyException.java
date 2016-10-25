package com.user;

/**
 * Indicates that user that was tried to be created
 * already exists in system
 * Created by samlinz on 26.10.2016.
 */
public class UserExistsAlreadyException extends Exception {
    public UserExistsAlreadyException(String username) {
        super("User " + username + " already exists!");
    }
}

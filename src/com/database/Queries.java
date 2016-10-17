package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Encloses the prepared statements for db connection
 * Created by samlinz on 15.10.2016.
 */
public class Queries {

    /*
        SQL queries
     */

    // fetch encrypted password for authentication
    private static String queryText_getUserPassword = "SELECT passwd FROM user WHERE name = ?";
    PreparedStatement query_getUserPassword;

    // insert new user
    private static String queryText_registerNewUser = "INSERT INTO user(name, passwd) VALUES" +
            "(?, ?, CURDATE())";
    PreparedStatement query_registerNewUser;

    // does username already exists
    private static String queryText_getUserNameExists = "SELECT name FROM user WHERE name = ?";
    PreparedStatement query_getUserNameExists;

    // fetch words in list
    private static String queryText_getWords = "SELECT * FROM word WHERE EXISTS (SELECT * FROM wordEntry" +
            "WHERE idList = ? AND idWord = word.idWord LIMIT 1)";
    PreparedStatement query_getUserPassword;

    private static boolean isInitialized;

    /**
     * Initialize the statements after the connection has been created
     * @param conn Connection object
     */
    public static void initStatements(Connection conn) {
        try {
            query_getUserPassword   = conn.prepareStatement(queryText_getUserPassword);
            query_registerNewUser   = conn.prepareStatement(queryText_registerNewUser);
            query_getUserNameExists = conn.prepareStatement(queryText_getUserNameExists);
            query_getWords          = conn.prepareStatement(queryText_getWords);
        } catch (SQLException e) {
            LOG.warning("Failed to initialize SQL queries");
            e.printStackTrace();
            isInitialized = false;
            return;
        }
        isInitialized = true;
    }

    /**
     *
     * @return boolean true if the statements have been initialized
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    static {
        isInitialized = false;
    }
}

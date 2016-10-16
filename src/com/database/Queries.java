package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Encloses the prepared statements for db connection
 * Created by samlinz on 15.10.2016.
 */
public class Queries {

    /*
        SQL queries
     */

    private static boolean isInitialized;

    /**
     * Initialize the statements after the connection has been created
     * @param conn Connection object
     */
    public static void initStatements(Connection conn) {
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

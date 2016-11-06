package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Encloses the prepared statements for db connection
 * Created by samlinz on 15.10.2016.
 */
public class Queries {

    // logger
    private static Logger LOG;

    // hashmap of queries
    private static Map<Query, PreparedStatement> queries;
    // flag to indicate initialization status
    private static boolean isInitialized;

    /**
     * Initialize the statements after the connection has been created
     *
     * @param conn Connection object
     */
    public static void initStatements(Connection conn) {
        queries = new EnumMap<>(Query.class);

        try {
            for (Query q : Query.values()) {
                queries.put(q, conn.prepareStatement(q.getSQL()));
            }
        } catch (SQLException e) {
            LOG.warning("Failed to initialize SQL queries");
            e.printStackTrace();
            isInitialized = false;
            return;
        }
        isInitialized = true;
    }

    /**
     * Fetch a prepared statement object
     *
     * @param query query object
     * @return PreparedStatement if found, null otherwise
     */
    public static PreparedStatement getQuery(Query query) {
        return queries.get(query);
    }

    /**
     * @return boolean true if the statements have been initialized
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Closes all PreparedStatements!
     */
    public static void closeQueries() {
        for (Map.Entry w : queries.entrySet()) {
            try {
                ((PreparedStatement) w.getValue()).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LOG.info("Closed all queries");
    }

    static {
        isInitialized = false;
        LOG = Logger.getLogger(Queries.class.getName());
    }
}

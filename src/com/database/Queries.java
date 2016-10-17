package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
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
    private static Map<String, PreparedStatement> queries;

    /*
        SQL queries
     */
    // fetch encrypted password for authentication
    private static String queryText_getUserPassword =
            "SELECT passwd FROM user WHERE name = ?;";
    // insert new user
    private static String queryText_registerNewUser =
            "INSERT INTO user(name, passwd) VALUES" +
            "(?, ?, CURDATE());";
    // does username already exists
    private static String queryText_getUserNameExists =
            "SELECT name FROM user WHERE name = ?;";
    // fetch words in list
    private static String queryText_getWords =
            "SELECT * FROM word WHERE EXISTS (SELECT * FROM wordEntry" +
            "WHERE idList = ? AND idWord = word.idWord LIMIT 1);";
    // add word to db
    private static String queryText_addWord =
            "INSERT INTO word(word, idMaster, idLang)" +
            "SELECT ?, master.idWord, lang.idLanguage FROM" +
            "word AS master, language AS lang WHERE" +
            "word.idWord = ? AND lang.idLang = ?;";
    // create a new empty list
    private static String queryText_createList =
            "INSERT INTO list(name, creator) VALUES(?, ?);";
    // create a word entry for list
    private static String queryText_createWordEntry =
            "INSERT INTO wordEntry(idWord, idList) " +
            "SELECT word.idWord, list.idList FROM word, list" +
            "WHERE word.idWord = ? AND list.idList = ?;";
    // fetch result by id
    private static String queryText_getResults =
            "SELECT * FROM result WHERE idResult = ?;";
    // fetch user's results
    private static String queryText_getUserResult =
            "SELECT * FROM result WHERE idUser = ?;";
    // fetch user's results from a time interval
    private static String queryText_getUserResultsFromTimeInterval =
            "SELECT * FROM result WHERE idUser = ? AND result.date BETWEEN ? AND ?;";
    // create a new result entry to database
    private static String createResult =
            "INSERT INTO result(date, idUser, score, idLang, playTime) " +
            "VALUES(?, ?, ?, ?, ?);";

    /* SQL queries end */

    // flag to indicate initialization status
    private static boolean isInitialized;

    /**
     * Initialize the statements after the connection has been created
     * @param conn Connection object
     */
    public static void initStatements(Connection conn) {
        queries = new HashMap<>();

        try {
            queries.put("getUserPassword",      conn.prepareStatement(queryText_getUserPassword));
            queries.put("registerNewUser",      conn.prepareStatement(queryText_registerNewUser));
            queries.put("getUserNameExists",    conn.prepareStatement(queryText_getUserNameExists));
            queries.put("getWords",             conn.prepareStatement(queryText_getWords));
            queries.put("addWord",              conn.prepareStatement(queryText_addWord));
            queries.put("createList",           conn.prepareStatement(queryText_createList));
            queries.put("createWordEntry",      conn.prepareStatement(queryText_createWordEntry));
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
     * @param name Name of the query
     * @return PreparedStatement if found, null otherwise
     */
    public static PreparedStatement getQuery(String name) {
        return queries.get(name);
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
        LOG = Logger.getLogger(Queries.class.getName());
    }
}

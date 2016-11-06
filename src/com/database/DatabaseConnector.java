package com.database;

import com.word.lang.LanguageProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by samlinz on 15.10.2016.
 */
public class DatabaseConnector {

    private static final Logger LOG;
    private final boolean gotConnector;

    private String sUser = "user";
    private String sPasswd = "kebab";

    public static String dbName = "wordSystem";
    public static String sAddress = "localhost";
    public static int sPort = 3306;
    private Connection conn = null;

    // connector instance, only one required per system
    private static DatabaseConnector connectorInstance;

    /**
     * Constructor
     */
    private DatabaseConnector() {
        // Get logger
        // Register connector
        gotConnector = getConnector();
    }

    /**
     * Register a JDBC connector
     *
     * @return boolean indicating the success of registering the connector
     */
    private boolean getConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            LOG.warning("Failed to get the DB connector class");
            return false;
        }

        return true;
    }

    /**
     * Shortcut for opening connection with hardcoded default values for server
     *
     * @return true if the connection was successfull
     */
    public boolean connect() {
        return connect(sAddress, sPort, sUser, sPasswd);
    }

    /**
     * Connect to a database
     */
    public boolean connect(String address, int port, String user, String passwd) {

        if (address == null || port == -1) {
            LOG.warning("Cannot connect to a database; the database configuration is not set");
            return false;
        }

        // set up the db config and user info
        String URL = "jdbc:mysql://" + address + "/" + dbName;
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", passwd);

        // connect to db and get the object
        try {
            conn = DriverManager.getConnection(URL, info);
        } catch (SQLException e) {
            LOG.warning("Could not load JDBC driver!");
            return false;
        }

        LOG.info("Connected to database server at " + address);

        // initialize prepared statements
        Queries.initStatements(conn);

        buildSystemState();

        return true;
    }

    /**
     * Some things are required to be loaded beforehand,
     * like language data
     */
    public void buildSystemState() {
        LOG.info("Building system state...");
        LanguageProvider.fetchLanguages();
    }

    /**
     * @return boolean indicating the status of connection
     */
    public boolean isConnected() {
        if (conn != null) {
            try {
                return !conn.isClosed();
            } catch (SQLException e) {
                LOG.warning("Querying db server status failed");
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                Queries.closeQueries();
            } catch (SQLException e) {
                LOG.warning("Could not close db connection");
                e.printStackTrace();
                return;
            }
        }
        if (!isConnected()) {
            LOG.info("Database connection closed");
        }
    }

    /**
     * Remove connector instance so a new can be fetched
     * System uses only one instance but unit tests have their own
     */
    public void removeInstance() {
        if (!isConnected()) {
            LOG.info("Connection still open, closing..");
            closeConnection();
        }
        connectorInstance = null;
        LOG.info("Db connector instance removed");
    }

    /**
     * Static method for receiving the instance of the connector
     * or initializing new with default values if does not exists
     *
     * @return
     */
    public static DatabaseConnector getInstance() {
        if (connectorInstance == null) {
            LOG.info("No db connector, initializing new...");
            LOG.info("address " + sAddress + ":" + sPort);
            connectorInstance = new DatabaseConnector();
        }

        return connectorInstance;
    }

    /**
     * Set db server config
     *
     * @param address
     * @param port
     * @param user
     * @param pwd
     */
    public void setDbServer(String address, int port, String user, String pwd) {
        sAddress = address;
        sPort = port;
        sUser = user;
        sPasswd = pwd;
        LOG.info("Set up server config");
    }


    /**
     * Set db server ip and port
     *
     * @param address ip address
     * @param port    port
     */
    public void setDbServer(String address, int port) {
        setDbServer(address, port, sUser, sPasswd);
    }

    /**
     * Sanitize string to be used as query to prevent injection
     *
     * @param str
     * @return string which has been trimmed and stripped of malicious special characters
     */
    public static String sanitizeString(String str) {
        return str
                .trim()
                .toLowerCase()
                .replace("'", "")
                .replace(";", "")
                .replace("(", "")
                .replace(")", "");
    }

    // fetch class logger
    static {
        LOG = Logger.getLogger(DatabaseConnector.class.getName());
    }
}

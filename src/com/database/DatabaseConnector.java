import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by samlinz on 15.10.2016.
 */
public class DatabaseConnector {

    private final Logger LOG;
    private final boolean gotConnector;

    private final static String defUser = "user";
    private final static String defPasswd = "kebab";

    public static final String dbName = "wordSystem";
    public static final String defAddress = "localhost";
    public static final int defPort = 3306;
    private Connection conn;

    /**
     * Constructor
     */
    private DatabaseConnector() {
        // Get logger
        LOG = Logger.getLogger(DatabaseConnector.class.getName());
        // Register connector
        gotConnector = getConnector();
    }

    /**
     * Register a JDBC connector
     * @return boolean indicating the success of registering the connector
     */
    private boolean getConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException ex) {
            LOG.warning("Failed to get the DB connector class");
            return false;
        }

        return true;
    }

    /**
     * Shortcut for opening connection with hardcoded default values for server
     * @return true if the connection was successfull
     */
    public boolean connect() {
        return connect(defAddress, defPort, defUser, defPasswd);
    }

    /**
     * Connect to a database
     */
    public boolean connect(String address, int port, String user, String passwd) {

        if(address == null || port == -1) {
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

        LOG.info("Connected to server at " + address);


        // initialize prepared statements
        Queries.initStatements(conn);

        return true;
    }

    /**
     * Static method for receiving the instance of the connector
     * @return
     */
    public static DatabaseConnector getInstance() {

    }

    public static void main(String... args) {
        // TESTI
        DatabaseConnector asd = new DatabaseConnector();
        asd.connect();
    }
}

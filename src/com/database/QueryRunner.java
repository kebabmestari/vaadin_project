package com.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Run SQL queries and automatically manage exceptions and connection handling
 * Created by samlinz on 29.10.2016.
 */
public class QueryRunner {
    // the prepared statement object
    private PreparedStatement st;

    // query result set
    private ResultSet rs;

    // param counter
    private int param;
    private int maxParam;

    // identifier
    private String qrName;


    // constructor
    private QueryRunner(Query query) {
        st = Queries.getQuery(query);
        this.param = 0;
        qrName = query.toString();
        try {
            this.maxParam = st.getParameterMetaData().getParameterCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.info(qrName + ": Query runner initialized");
    }

    /**
     * Run the query and fetch the result set
     * Results can be fetched with getResults
     */
    public void run() {
        if(this.param < this.maxParam) {
            LOG.warning(qrName + ": Cannot run query, all parameters are not set!");
            return;
        }
        try {
            rs = st.executeQuery();
        } catch (SQLException e) {
            LOG.warning(qrName + ": Failed to run query. Closing..");
            e.printStackTrace();
            close();
        }
    }

    /**
     * Run an update to the database
     * Does not return any results
     */
    public void runUpdate() {
        if(this.param < this.maxParam) {
            LOG.warning(qrName + ": Cannot run query, all parameters are not set!");
            return;
        }
        try {
            int updates = st.executeUpdate();
            if(updates > 0) {
                LOG.info(qrName + ": Successfully updated " + updates + " entries");
            } else {
                LOG.warning(qrName + ": Failed to run updates");
            }
        } catch (SQLException e) {
            LOG.warning(qrName + ": Failed to run query. Closing..");
            e.printStackTrace();
            close();
        }
    }

    /**
     * Get result size in rows
     * @return integer number of rows
     */
    public int getResultRows() {
        if(rs == null) {
            return -1;
        }
        int count = 0;
        try {
            while(rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Return list of single column's data
     * @param column column number
     * @return arraylist of row values
     */
    public <T> List<T> getResults(Class<T> caster, String column) {
        // move cursor to beginning

        LOG.info(qrName + ": Fetching results of column " + column);

        List<T> result = new ArrayList<>();

        if(rs == null) {
            LOG.warning(qrName + ": ResultSet not fetched, run query first!");
            return null;
        }

        int rowCount = 0;

        try {
            rs.beforeFirst();
            // fetch the single column from each row=
            while(rs.next()) {
                result.add(caster.cast(rs.getObject(column)));
                rowCount++;
            }
            LOG.info(qrName + ": Fetched " + rowCount + " values");
        } catch (SQLException e) {
            LOG.warning(qrName + ": Failed to fetch result column");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Set a parameter for prepared query
     * @param value the value
     * @param <T> type
     */
    public <T> void setParam(T value) {
        param++;
        try {
            if(value instanceof String) {
                st.setString(param, (String) value);
            } else if (value instanceof Integer) {
                st.setInt(param, (Integer) value);
            } else {
                LOG.warning("Unsupported type of parameter for SQL query");
            }
        } catch (SQLException e) {
            LOG.warning("Unable to set parameter");
            e.printStackTrace();
        }
    }

    /**
     * Closes the connected objects
     */
    public void close() {
        try {
            // There should probably be only one statement open so
            // we don't close it until the system closes!
//            if(st != null) {
//                st.close();
//                st = null;
//            }
            if(rs != null) {
                rs.close();
                rs = null;
            }
            LOG.info(qrName + ": Query runner closed");
        } catch (SQLException e) {
            LOG.warning(qrName + ": Failed to close query runner");
            e.printStackTrace();
        }
    }

    /**
     * Get instance of runner
     * @return queryrunner
     */
    public static QueryRunner getRunner(Query query) {
        return new QueryRunner(query);
    }

    // logger
    private static Logger LOG = Logger.getLogger(QueryRunner.class.getName());
}

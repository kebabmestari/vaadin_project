package com.database;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for database connector
 * Created by samlinz on 17.10.2016.
 */
public class DatabaseConnectorTest {

    // test configuration, use test account!
    public static final String address = "localhost";
    public static final int port = 3306;
    public static final String user = "unitTester";
    public static final String passwd = "kebab";

    private DatabaseConnector dbConn;

    @Before
    public void setUp() throws Exception {
        dbConn = DatabaseConnector.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        dbConn.closeConnection();
        dbConn.removeInstance();
    }

    @Test
    public void testConnection() throws Exception {
        System.out.println("Testing connection to localhost");
        dbConn.setDbServer(address, port, user, passwd);
        dbConn.connect();
        Assert.assertTrue(dbConn.isConnected());
    }

}
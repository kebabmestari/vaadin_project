package com.user;

import com.database.DatabaseConnector;
import com.database.Queries;
import com.database.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by samlinz on 25.10.2016.
 */
public class UserTest {

    private DatabaseConnector conn;

    @Test
    public void userExists() throws Exception {
        // test whether test user exists
        Assert.assertTrue(UserProvider.userExists("unitTester"));
    }

    @Test
    public void passwordMatch() throws Exception {
        String pwd = com.database.StringCrypt.encrypt("kebab");
        PreparedStatement sta = Queries.getQuery(Query.GET_USER_INFO);
        sta.setString(1, "unitTester");
        ResultSet res = sta.executeQuery();
        res.next();
        Assert.assertTrue(res.getString("passwd").equals(pwd));
    }

    @After
    public void tearDown() throws Exception {
        conn.closeConnection();
        conn.removeInstance();
    }

    @Before
    public void setUp() throws Exception {
        // connect to local test db
        conn = DatabaseConnector.getInstance();
        conn.connect();
    }
}
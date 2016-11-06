package com.user;

import com.database.*;
import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by samlinz on 25.10.2016.
 */
public class UserTest {

    private static DatabaseConnector conn;

    @Test
    public void userExists() throws Exception {
        // test whether test user exists
        Assert.assertTrue(UserProvider.userExists("unitTester"));
    }

    @BeforeClass
    public static void newUserCreationWorks() throws Exception {
        // connect to local test db
        conn = DatabaseConnector.getInstance();
        conn.connect(DatabaseConnectorTest.address, DatabaseConnectorTest.port
                , DatabaseConnectorTest.user, DatabaseConnectorTest.passwd);

        Assert.assertTrue(UserFactory.createNewUserAndFlushToDatabase("unittester", "kebab"));
    }

    @AfterClass
    public static void removeTestUser() throws Exception {
        QueryRunner qr = QueryRunner.getRunner(Query.REMOVE_USER);
        qr.setParam("unittester");
        Assert.assertTrue(qr.runUpdate() > 0);
        conn.closeConnection();
        conn.removeInstance();
    }

    @Test
    public void authenticateUser() {
        Assert.assertTrue(UserProvider.authenticateUser("unittester", "kebab"));
    }

    @Test
    public void passwordMatch() throws Exception {
        String pwd = com.database.StringCrypt.encrypt("kebab");
        PreparedStatement sta = Queries.getQuery(Query.GET_USER_INFO);
        sta.setString(1, "unittester");
        sta.setInt(2, -1);
        ResultSet res = sta.executeQuery();
        res.next();
        Assert.assertTrue(res.getString("passwd").equals(pwd));
    }
}
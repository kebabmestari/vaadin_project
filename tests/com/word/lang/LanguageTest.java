package com.word.lang;

import com.database.DatabaseConnector;
import com.database.DatabaseConnectorTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by samlinz on 30.10.2016.
 */
public class LanguageTest {

    DatabaseConnector conn;

    @Test
    public void fetchLanguages() throws Exception {
        LanguageProvider.fetchLanguages();
        Language lan = LanguageProvider.getLanguage(1);
        Assert.assertNotNull(lan);
        Assert.assertEquals("finnish", lan.getName());
        LanguageProvider.removeLanguages();
    }

    @Test
    public void getLanguage() throws Exception {
        LanguageProvider.addLanguage("test language", 999);
        Assert.assertEquals("test language", LanguageProvider.getLanguage(999).getName());
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
        conn.connect(
                DatabaseConnectorTest.address,
                DatabaseConnectorTest.port,
                DatabaseConnectorTest.user,
                DatabaseConnectorTest.passwd
        );
    }

}
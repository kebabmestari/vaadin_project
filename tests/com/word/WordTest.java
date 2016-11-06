package com.word;

import com.database.DatabaseConnector;
import com.database.DatabaseConnectorTest;
import com.word.lang.Language;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test word and related classes
 * Created by samlinz on 28.10.2016.
 */
public class WordTest {

    DatabaseConnector conn;

    @Test
    public void getWord() throws Exception {
        Language testLang = new Language();
        Word w = WordFactory.getWord(1, "testi", testLang, null);
        Assert.assertEquals("testi", w.getWord());
        Assert.assertEquals(1, w.getId());
        Assert.assertEquals(testLang, w.getLang());
    }

    @Test
    public void testWordFlush() throws Exception {
        Word w = null;
        Word w2 = null;
        try {
            Language testLang = new Language();
            testLang.setId(1);
            w = WordFactory.getWord(99999, "testi", testLang, null);
            WordProvider.flush(w);
            Set<Word> testSet = new HashSet<>();
            testSet.add(w);
            w2 = WordFactory.getWord(99990, "testi", testLang, testSet);
            WordProvider.flush(w2);
            Word w3 = WordProvider.getWord(99990);
            Assert.assertNotNull(w3);
            Assert.assertEquals(w.getWord(), w3.getWord());
        } finally {
            if(w2 != null)
                WordProvider.deleteWord(w2);
            if(w != null)
                WordProvider.deleteWord(w);
        }
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
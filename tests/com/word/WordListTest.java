package com.word;

import com.database.DatabaseConnector;
import com.database.DatabaseConnectorTest;
import com.user.User;
import com.user.UserFactory;
import com.word.lang.Language;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test word lists
 * Created by samlinz on 30.10.2016.
 */
public class WordListTest {

    DatabaseConnector conn;

    @Test
    public void buildWordList() throws Exception {
        User testUser = UserFactory.createUser("testuser", 1, new Date());
        List<Word> words = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Word newWord = WordFactory.getWord(i, "testi" + i, new Language(), null);
            words.add(newWord);
        }
        WordList newList = WordListFactory.getWordList(999, "testlist", testUser, 10, words);
        WordListProvider.flushToDatabase(newList);
        WordListBuilder.buildWordList(999);
        WordList newList2 = WordListProvider.getList(999);

        Assert.assertNotNull(newList2);
        Assert.assertEquals(newList2.getName(), "testlist");
        Assert.assertEquals(10, newList2.getWordCount());

    }

    @Test
    public void createWordList() throws Exception {
        WordList test = new WordList();
        Language testLang = new Language();
        testLang.setId(1);
        for (int i = 0; i < 10; i++) {
            Word newWord = WordFactory.getWord(i, "testi" + i, testLang, null);
            test.addWord(newWord);
        }
        test.setId(1);
        test.setName("test list please ignore");

        Assert.assertNotNull(test.getWords());
        Assert.assertEquals(test.getWords().size(), 10);

        Assert.assertEquals(test.getName(), "test list please ignore");
    }

    @Test


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
package com;

import com.database.DatabaseConnector;
import com.database.DatabaseConnectorTest;
import com.game.Result;
import com.game.RoundInstance;
import com.game.RoundOver;
import com.user.User;
import com.user.UserFactory;
import com.word.*;
import com.word.lang.Language;
import com.word.lang.LanguageProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by adminpc on 31/10/2016.
 */
public class systemTest {
    private DatabaseConnector conn;

    private User user;
    private WordList list;
    private Language lang;
    private List<Word> words;
    private List<Word> wordExplanations;
    private RoundInstance round;
    private Result result;

    // create user
    public void createUser() {
        System.out.println("CREATING USER");
        user = UserFactory.createUser("systemtester", 999, new Date());
        Assert.assertNotNull(user);
    }

    // create language
    public void createLanguage() {
        lang = LanguageProvider.getLanguage(1);
        Assert.assertNotNull(lang);
    }

    // create words
    public void createWords(int wordCount) {
        words = new ArrayList<>();
        System.out.println("CREATING WORDS");
        Word[] wordArr1 = new Word[wordCount];
        Word[] wordArr2 = new Word[wordCount];
        for (int i = 0; i < wordCount; i++) {
            Word newWord = WordFactory.getWord(1000+i, "explanation" + i, lang, null);
            wordArr1[i] = newWord;
        }
        for (int i = 0; i < wordCount; i++) {
            Set<Word> masters = new HashSet<>();
            masters.add(wordArr1[i]);
            Word newWord = WordFactory.getWord(2000 + i, "systemtest" + i, lang, masters);
            words.add(newWord);
        }
        Assert.assertEquals(wordCount, words.size());
        Assert.assertEquals(2000, words.get(0).getId());
    }

    // create words list
    public void createList() {
        list = WordListFactory.getWordList(1, "testlist", user, 10, words);
        Assert.assertNotNull(list);
        Assert.assertEquals(10, list.getWordCount());
        Assert.assertEquals(2000 ,list.getWords().get(0).getId());
    }

    // create game instance
    public void createGame() throws Exception {
        round = WordListProvider.getInstance(list);
        int counter = 0;
        try {
            for (int i = 0; i < 100; i++) {
                Word word = round.getNextWord();
                System.out.println("Got word: " + word.getWord() + " id " + word.getId() + " lang " + word.getLang().getName());
                System.out.println("Word explanation: " + word.getMasters().iterator().next().getWord().toUpperCase());
                Assert.assertTrue(round.guessWord(word.getMasters().iterator().next().getWord().toUpperCase(),
                        word));
                round.addScore();
                counter++;
            }
            throw new Exception("SHOULD NOT GET HERE");
        } catch (RoundOver e) {
            Assert.assertEquals(round.getMasterList().getWordCount(), counter);
            System.out.println("Round over, score " + round.getScore());
        }
        Assert.assertTrue(round.isOver());
    }

    @Test
    public void runSystemTest() throws Exception {
        createUser();
        createLanguage();
        createWords(10);
        createList();
        createGame();
    }

    @After
    public void tearDown() throws Exception {
        conn.closeConnection();
        conn.removeInstance();
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("Running a whole system test");
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

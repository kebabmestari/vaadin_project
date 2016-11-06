package com.game;

import com.word.WordList;
import com.word.WordListProvider;
import com.word.WordListTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test game round instance
 * Created by adminpc on 31/10/2016.
 */
public class RoundTest {
    RoundInstance round;
    WordList testList;

    @Before
    public void setUp() throws Exception {
        testList = WordListTest.getTestList();
        round = WordListProvider.getInstance(testList);
    }

    @After
    public void tearDown() throws Exception {
        round = null;
        testList = null;
    }

    @Test
    public void testAttributes() throws Exception {
        Assert.assertEquals(testList, round.getMasterList());
    }

    @Test(expected = RoundOver.class)
    public void testRoundOver() throws Exception {
        for (int i = 0; i <= testList.getWordCount()+1; i++) {
            round.getNextWord();
        }
    }

    @Test
    public void testGuess() throws Exception {
        int score = 0;
        for (int i = 0; i < testList.getWordCount(); i++) {

        }
    }

    @Test
    public void getWords() throws Exception {


    }
}
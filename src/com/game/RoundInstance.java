package com.game;

import com.word.Word;
import com.word.WordList;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

/**
 * An instance of word list in usage
 * Supplies new words and tracks the score
 * Created by samlinz on 17.10.2016.
 */
public class RoundInstance {

    // list of which this object is related to
    private WordList masterList;
    // logger
    private static Logger LOG;
    // list of appeared words
    private final Set<Word> appeared;
    // rng
    private Random randomGenerator;
    // score for this round
    private int score;
    // word number
    private int wordNumber;

    // cannot instatiate explicitly
    public RoundInstance() {
        // init word set
        this.appeared = new HashSet<>();
        this.randomGenerator = new Random();
        this.wordNumber = 1;
        this.score = 0;
    }

    // return master list
    private WordList getMasterList() {
        return masterList;
    }

    // set master list
    private void setMasterList(WordList masterList) {
        this.masterList = masterList;
    }

    /**
     * Return a random word from the list
     * @return Word object
     */
    public Word getNextWord() {

        if(isOver()) {

        }

        List<Word> words = masterList.getWords();
        Word word = words.get(randomGenerator.nextInt(words.size()));
        appeared.add(word);
        LOG.info("Picked word " + word.getWord());

        wordNumber++;

        return word;
    }

    public void setMaster(WordList list) {
        this.masterList = list;
    }

    /**
     * @return true if this word is the last in round
     */
    public boolean isOver() {
        return wordNumber > masterList.getWordCount();
    }

    static {
        LOG = Logger.getLogger(RoundInstance.class.getName());
    }
}

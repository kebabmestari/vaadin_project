package com.word;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A wrapper for a word object list with some utility functions
 * Created by samlinz on 17.10.2016.
 */
public class WordList {

    // logger
    private static Logger LOG;

    // set of words (word entry in database) in the list
    private Set<Word> words;

    // constructor
    public WordList() {
        words = new HashSet<>();
        LOG.fine("Word list created");
    }

    /**
     * @param word
     * @return true if the list already contains the word
     */
    public boolean hasWord(Word word) {
        return words.contains(word);
    }

    /**
     * Append a word into list
     *
     * @param word
     */
    public void addWord(Word word) {
        words.add(word);
    }

    /**
     * @return word count
     */
    public int getCount() {
        return words.size();
    }

    /**
     * Write a new word list into database
     */
    void flushToDataBase() {
        // FIXME: 17.10.2016 DO THIS
    }

    static {
        LOG = Logger.getLogger(WordList.class.getName());
    }
}

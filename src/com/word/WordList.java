package com.word;

import com.user.User;

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
    // max score
    private int maxScore;
    // list id
    private int id;
    // list name
    private String name;
    // creator
    private User creator;

    // constructor
    public WordList() {
        words = new HashSet<>();
        LOG.fine("Word list created");
    }

    /* Getters and setters */

    public int getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public int getMaxScore() {
        return maxScore;
    }

    void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    /* Utility methods */

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
    public int getWordCount() {
        return words.size();
    }

    /**
     * Write a new word list into database
     */
    void flushToDataBase() {
        // FIXME: 17.10.2016 DO THIS
        LOG.info("Flushed word list " + this.name);
    }

    static {
        LOG = Logger.getLogger(WordList.class.getName());
    }
}

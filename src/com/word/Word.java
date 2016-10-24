package com.word;

import com.word.lang.Language;

import java.util.logging.Logger;

/**
 * A word in system
 * Created by samlinz on 17.10.2016.
 */
public class Word {

    // logger
    public static Logger LOG;

    // word string itself
    private String word;
    // identifier
    private int id;

    // if it has a master, then it needs a reference to it
    private Word master;
    // word language
    private Language lang;

    // constructor
    Word() {
        LOG.fine("Word " + this.word + " created");
    }

    /* Getters and setters */

    // word string

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    // master/explanation word

    public Word getMaster() {
        return master;
    }
    public void setMaster(Word master) {
        this.master = master;
    }

    // language

    public Language getLang() {
        return lang;
    }
    public void setLang(Language lang) {
        this.lang = lang;
    }

    // integer indentifier

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return true if this word is a root word, aka has no translation
     */
    public boolean isRoot() {
        return (master == null);
    }

    static {
        LOG = Logger.getLogger(Word.class.getName());
    }
}

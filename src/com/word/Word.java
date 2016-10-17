package com.word;

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
    // boolean indicating if this word has an explanation/master or if it's a root word
    private boolean hasMaster;
    // if it has a master, then it needs a reference to it
    private Word master;
    // word language
//    private Language lang;

    // constructor
    public Word() {
        LOG.fine("Word " + this.word + " created");
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    private boolean hasMaster() {
        return hasMaster;
    }

    public Word getMaster() {
        return master;
    }

    public void setMaster(Word master) {
        this.master = master;
        this.hasMaster = true;
    }

    static {
        LOG = Logger.getLogger(Word.class.getName());
    }
}

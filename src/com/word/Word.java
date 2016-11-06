package com.word;

import com.word.lang.Language;

import java.util.HashSet;
import java.util.Set;
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

    // if it has a master(s), then it needs a reference to them
    private Set<Word> masters;
    // word language
    private Language lang;

    // constructor
    Word() {
        masters = new HashSet<>();
    }

    /* Getters and setters */

    // word string

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word.toLowerCase();
    }

    // master/explanation word

    public Set<Word> getMasters() {
        return masters;
    }

    public void addMaster(Word master) {
        this.masters.add(master);
    }

    public void setMasters(Set<Word> masters) {
        this.masters.addAll(masters);
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
        return (masters.size() == 0);
    }

    /**
     * Iterates through the master words and checks if given
     * string is a correct translation for this word
     *
     * @param word answered word as string
     * @return true if the answer is correct
     */
    public boolean isExplanation(String word) {
        word = word.toLowerCase();
        for (Word w : this.masters) {
            if (w.getWord().equals(word)) {
                return true;
            }
        }
        return false;
    }

    static {
        LOG = Logger.getLogger(Word.class.getName());
    }
}

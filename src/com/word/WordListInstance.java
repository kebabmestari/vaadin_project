package com.word;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

/**
 * An instance of word list in usage, to track the
 * already appeared words
 * Created by samlinz on 17.10.2016.
 */
public class WordListInstance {

    // list of which this object is related to
    private WordList masterList;
    // logger
    private static Logger LOG;
    // list of appeared words
    private final Set<Word> appeared;
    // rng
    private Random randomGenerator;

    // cannot instatiate explicitly
    private WordListInstance() {
        // init word set
        appeared = new HashSet<>();

        randomGenerator = new Random();
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
        List<Word> words = masterList.getWords();
        Word word = words.get(randomGenerator.nextInt(words.size()));
        appeared.add(word);
        LOG.info("Picked word " + word.getWord());
        return word;
    }

    /**
     * Create an instance of word list
     * @param list
     * @return word list instance tracker
     */
    public static WordListInstance getInstance(WordList list) {
        WordListInstance result = new WordListInstance();
        // FIXME: 17.10.2016 CONNECT TO SESSION!
        if(list == null) {
            LOG.warning("Invalid list given, null pointer");
            return null;
        }
        result.setMasterList(list);
        return result;
    }

    static {
        LOG = Logger.getLogger(WordListInstance.class.getName());
    }
}

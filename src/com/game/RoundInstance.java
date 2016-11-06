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
 * Represents an instance of the game
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
        this.wordNumber = 0;
        this.score = 0;
        LOG.info("Initializing round");
    }

    // return master list
    public WordList getMasterList() {
        return masterList;
    }

    // set master list
    private void setMasterList(WordList masterList) {
        this.masterList = masterList;
        LOG.info("Initializing round to list " + masterList.getName());
    }

    /**
     * Return a random word from the list
     *
     * @return Word object
     */
    public Word getNextWord() throws RoundOver {

        if (isOver()) {
            throw new RoundOver();
        }

        List<Word> words = masterList.getWords();
        Word word = null;
        do {
            if(appeared.size() >= masterList.getWordCount()) {
                LOG.info("Round over because all words iterated! Change your maxword attribute!");
                throw new RoundOver();
            }
            word = words.get(randomGenerator.nextInt(words.size()));
        } while (appeared.contains(word));
        appeared.add(word);
        LOG.info("Picked word " + word.getWord());

        wordNumber++;

        return word;
    }

    /**
     * Check if the guessed word was correct
     *
     * @param guess  guessed word as string
     * @param actual the actual word
     * @return true if correct
     */
    public static boolean guessWord(String guess, Word actual) {
        boolean result = false;
        guess = guess.toLowerCase();
        Set<Word> masters = actual.getMasters();
        for(Word w : masters) {
            if(w.getWord().equals(guess)) {
                result = true;
                break;
            }
        }
        if (!result) {
            final Set<Word> masters1 = actual.getMasters();
            LOG.info("Wrong answer. Actual: " + masters1 == null ? "null" : masters1.toString());
        } else {
            LOG.info("Correct answer");
        }
        return result;
    }

    /**
     * Increments score by one
     */
    public void addScore() {
        this.score++;
        LOG.info("Icremented score by one");
    }

    /**
     * @return round score
     */
    public int getScore() {
        return this.score;
    }

    public void setMaster(WordList list) {
        this.masterList = list;
    }

    /**
     * @return true if this word is the last in round
     */
    public boolean isOver() {
        return
                (wordNumber > masterList.getMaxScore()) ||
                wordNumber > masterList.getWordCount();
    }

    static {
        LOG = Logger.getLogger(RoundInstance.class.getName());
    }
}

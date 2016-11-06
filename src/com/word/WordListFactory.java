package com.word;

import com.user.User;

import java.util.List;
import java.util.logging.Logger;

/**
 * Factory class to create new Word Lists
 * To fetch existing ones, see WordListBuilder
 * Created by samlinz on 30.10.2016.
 */
public class WordListFactory {
    public static WordList getWordList(int id, String name, User creator, int maxScore, List<Word> words) {

        WordList newList = new WordList();
        newList.setId(id);
        newList.setCreator(creator);
        newList.setMaxScore(maxScore);
        newList.setName(name);

        if (words.size() == 0) {
            LOG.info("Creating a list with no words");
        }

        newList.setWords(words);
        return newList;
    }

    private static Logger LOG = Logger.getLogger(WordListFactory.class.getName());
}

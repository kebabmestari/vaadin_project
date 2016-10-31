package com.word;

import com.word.lang.Language;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Factory for word objects
 * Created by samlinz on 28.10.2016.
 */
public class WordFactory {
    public static Word getWord(int id, String word, Language lang, Set<Word> masters) {
        if(word.length() <= 0) {
            LOG.warning("Invalid word string to WordFactory");
            return null;
        }
        if(lang == null) {
            throw new NullPointerException("Language given to WordFactory is null!");
        }
        Word newWord = new Word();
        newWord.setId(id);
        newWord.setLang(lang);
        if(masters != null)
            newWord.setMasters(masters);
        newWord.setWord(word);
        return newWord;
    }
    private static Logger LOG = Logger.getLogger(WordFactory.class.getName());
}

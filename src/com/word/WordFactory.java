package com.word;

import com.word.lang.Language;

import java.util.Set;

/**
 * Factory for word objects
 * Created by samlinz on 28.10.2016.
 */
public class WordFactory {
    public static Word getWord(int id, String word, Language lang, Set<Word> masters) {
        Word word = new Word();
        word.setId(id);
        word.setLang(lang);
        word.setMasters(masters);
        word.setWord(word);
        return word;
    }
}

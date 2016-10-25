package com.word.lang;

/**
 * A language which has words assigned to it
 * Created by samlinz on 25.10.2016.
 */
public class Language {
    // string identifier of the language
    private String name;
    // identifier
    private int id;

    // constructor
    Language(String name, int id) {
        this.name = name.toLowerCase();
        this.id = id;
    }

}
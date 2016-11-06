package com.word.lang;

/**
 * A language which has words assigned to it
 * Created by samlinz on 25.10.2016.
 */
public class Language implements Tag {

    // string identifier of the language
    private String name;
    // identifier
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
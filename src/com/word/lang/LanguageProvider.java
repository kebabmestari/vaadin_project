package com.word.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to retvieve and add languages
 * Created by samlinz on 26.10.2016.
 */
public class LanguageProvider {
    private static Map<String, Language> languageMap = new HashMap<>();

    /**
     * Add a new language to the map
     * @param name name of the language
     * @param id id of the language
     */
    public static void addLanguage(String name, int id) {
        name = name.toLowerCase();
        if(!languageMap.containsKey(name)) {
            Language newlang = new Language(name, id);
            languageMap.put(name, newlang);
        }
    }

    public static Language getLanguage(String name) {
        return languageMap.get(name.toLowerCase());
    }
}

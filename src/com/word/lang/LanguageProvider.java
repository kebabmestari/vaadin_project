package com.word.lang;

import com.database.Queries;
import com.database.Query;
import com.database.QueryRunner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Utility class to retvieve and add languages
 * Created by samlinz on 26.10.2016.
 */
public class LanguageProvider {
    private static Map<Integer, Language> languageMap = new HashMap<>();

    /**
     * Add a new language to the map
     * @param name name of the language
     * @param id id of the language
     */
    public static void addLanguage(String name, int id) {
        name = name.toLowerCase();
        if(!languageMap.containsKey(name)) {
            Language newlang = new Language();
            newlang.setId(id);
            newlang.setName(name);
            languageMap.put(id, newlang);
            LOG.info("Added language " + name);
        }
    }

    /**
     * Fetch all languages to memory from db
     * no need to management, so few
     */
    public static void fetchLanguages() {
        QueryRunner qr = QueryRunner.getRunner(Query.GET_LANGUAGES);
        qr.run();
        List<Integer> langIds   = qr.getResults(Integer.class, "idLanguage");
        List<String> langNames  = qr.getResults(String.class, "name");
        for(int i = 0; i < langIds.size(); i++) {
            addLanguage(langNames.get(i), langIds.get(i));
        }
        qr.close();
    }

    /**
     * Retvieve a language object
     * @param id lang id
     * @return Language object if it exists, null otherwise
     */
    public static Language getLanguage(int id) {
        return languageMap.get(id);
    }

    /**
     * Clear language map
     */
    public static void removeLanguages() {
        languageMap.clear();
    }

    private static Logger LOG = Logger.getLogger(LanguageProvider.class.getName());
}

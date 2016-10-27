package com.word.lang;

import com.database.Queries;
import com.database.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
            LOG.fine("Added language " + name);
        }
    }

    /**
     * Fetch all languages to memory from db
     * no need to management, so few
     */
    public static void fetchLanguages() {
        final PreparedStatement st = Queries.getQuery(Query.GET_LANGUAGES);
        ResultSet rs = null;
        try {
            rs = st.executeQuery();
            while(rs.next()) {
                addLanguage(rs.getString("name"), rs.getInt("idLanguage"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(st != null) st.close();
                if(rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            LOG.info("Languages fetched into system");
        }
    }

    public static Language getLanguage(int id) {
        return languageMap.get(id);
    }
    private static Logger LOG = Logger.getLogger(LanguageProvider.class.getName());
}

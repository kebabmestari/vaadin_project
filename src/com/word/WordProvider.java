package com.word;

import com.database.Queries;
import com.database.Query;
import com.word.lang.LanguageProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides word objects
 * Created by samlinz on 28.10.2016.
 */
public class WordProvider {
    private static Map<Integer, Word> wordMap = new HashMap<>();

    /**
     * @param id word id
     * @return word object in system
     */
    public static Word getWord(int id) {
        Word result = wordMap.get(id);
        if(result == null) {
            fetchWord(id);
            return getWord(id);
        }
        return result;
    }

    /**
     * Fetch word from db
     * @param id word id
     */
    private static void fetchWord(int id) {
        if(wordMap.containsKey(id)) {
            return;
        }
        final PreparedStatement st = Queries.getQuery(Query.GET_WORD);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            Set<Word> masters = new HashSet<>();

            WordFactory.getWord(
                    id,
                    rs.getString("word"),
                    LanguageProvider.getLanguage(rs.getInt("idLang")),

                    )
        }
    }

    public static Set<Integer> getMasters(Word word) {
        Set<Integer> mastersIds = new HashSet<>();
        final PreparedStatement st = Queries.getQuery(Query.GET_WORD_MASTERS_IDS);
        ResultSet rs = null;
        try {
            rs = st.executeQuery();
            while(rs.next()) {
                mastersIds.add(rs.getInt("idMaster"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mastersIds;
    }

    /**
     * Fetch a list of word id:s
     * For multiple masters specifically
     * @param words Set of word id's as integers
     */
    private static void fetchWords(Set<Integer> words) {
        for(Integer w : words) {
            fetchWord((int)w);
        }
    }

    /**
     * Add a word into map
     * @param word word object
     */
    public static void addWordToMap(Word word) {
        wordMap.put(word.getId(), word);
    }
}

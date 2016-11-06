package com.word;

import com.database.Queries;
import com.database.Query;
import com.database.QueryRunner;
import com.word.lang.LanguageProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        if (result == null) {
            fetchWord(id);
            result = wordMap.get(id);
        }
        return result;
    }

    /**
     * @param word word
     * @return word object in system
     */
    public static Word getWord(String word) {
        Word result = null;
        for (int i = 0; i < 2; i++) {
            for (Map.Entry w : wordMap.entrySet()) {
                Word wor = (Word) w.getValue();
                if (wor.getWord().equals(word)) {
                    result = wor;
                    break;
                }
            }
            if (result == null) {
                fetchWord(word);
            }
        }
        return result;
    }

    /**
     * Find out if word exists in db
     *
     * @param word word string
     * @return true if word exists
     */
    public static boolean wordExists(String word) {
        QueryRunner qr = QueryRunner.getRunner(Query.GET_WORD_BY_STRING);
        qr.setParam(word);
        qr.run();
        return qr.getResultRows() > 0;
    }

    /**
     * Fetch word from db to server
     * Save it for faster access later on
     *
     * @param id word id
     */
    private static void fetchWord(int id) {

        // don't fetch a word which is fetched already
        if (wordMap.containsKey(id)) {
            return;
        }

        QueryRunner qr = QueryRunner.getRunner(Query.GET_WORD);
        qr.setParam(id);
        qr.run();

        if (qr.getResultRows() == 0) {
            return;
        }

        // create a new word
        Word newWord = WordFactory.getWord(
                qr.getResults(Integer.class, "idWord").get(0),
                qr.getResults(String.class, "word").get(0),
                LanguageProvider.getLanguage(
                        qr.getResults(Integer.class, "idLang").get(0)
                ),
                null
        );
        // append it to server map for faster refetch
        addWordToMap(newWord);

        // retvieve it's explanations aka master words
        Set<Integer> mas = getMasters(id);
        // fetch each master and create set
        Set<Word> mastersSet = mas.stream().map((i) -> getWord(i))
                .collect(Collectors.toCollection(HashSet::new));
        // set the masters
        newWord.setMasters(mastersSet);
    }

    /**
     * Fetch word from db to server
     * Save it for faster access later on
     *
     * @param word word
     */
    private static void fetchWord(String word) {

        QueryRunner qr = QueryRunner.getRunner(Query.GET_WORD_BY_STRING);
        qr.setParam(word);
        qr.run();

        if (qr.getResultRows() == 0)
            return;

        // create a new word
        Word newWord = WordFactory.getWord(
                qr.getResults(Integer.class, "idWord").get(0),
                qr.getResults(String.class, "word").get(0),
                LanguageProvider.getLanguage(
                        qr.getResults(Integer.class, "idLang").get(0)
                ),
                null
        );
        // append it to server map for faster refetch
        addWordToMap(newWord);

        // retvieve it's explanations aka master words
        Set<Integer> mas = getMasters(newWord.getId());
        // fetch each master and create set
        Set<Word> mastersSet = mas.stream().map((i) -> getWord(i))
                .collect(Collectors.toCollection(HashSet::new));
        // set the masters
        newWord.setMasters(mastersSet);
    }

    /**
     * Get id's of the explanations/masters of the given word
     *
     * @param id word id
     * @return set of id's
     */
    public static Set<Integer> getMasters(int id) {
        Set<Integer> mastersIds = new HashSet<>();
        final PreparedStatement st = Queries.getQuery(Query.GET_WORD_MASTERS_IDS);
        ResultSet rs = null;
        try {
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                mastersIds.add(rs.getInt("idMaster"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mastersIds;
    }

    /**
     * Search db for words beginning with the given stub
     *
     * @param beginning beginning of the word
     * @return list of matching strings
     */
    public static List<String> searchWords(String beginning) {
        // ignore case!
        beginning = beginning.toLowerCase();
        QueryRunner qr = QueryRunner.getRunner(Query.SEARCH_WORDS);
        qr.setParam(beginning);
        qr.run();
        // the results are strings
        List<String> words = qr.getResults(String.class, "word");
        qr.close();
        return words;
    }

    /**
     * Fetch a list of word ids
     * For multiple masters specifically
     *
     * @param words Set of word id's as integers
     */
    private static void fetchWords(Set<Integer> words) {
        for (Integer w : words) {
            fetchWord((int) w);
        }
    }

    /**
     * Flush word into db
     * @param word word object to be flushed
     */
    public static void flush(Word word) {
        QueryRunner qr = QueryRunner.getRunner(Query.CREATE_WORD);
        qr.setParam(word.getId());
        qr.setParam(word.getWord());
        qr.setParam(word.getLang().getId());
        qr.runUpdate();
        qr.close();
        LOG.info("Flushed word " + word.getWord() + " into db");
        // create explanation relations
        Set<Word> mastersSet = word.getMasters();
        if (mastersSet != null) {
            for (Word w : word.getMasters()) {
                qr = QueryRunner.getRunner(Query.CREATE_WORD_EXPLANATION);
                qr.setParam(word.getId());
                qr.setParam(w.getId());
                qr.runUpdate();
                qr.close();
            }
            LOG.info("Flushed word " + word.getWord() + " explanations into db");
        }
    }

    /**
     * For debugging purposes only!
     * Deletes from system and database
     */
    public static void deleteWord(Word word) {
        removeWordFromMap(word);
        QueryRunner qr = QueryRunner.getRunner(Query.DELETE_WORD_EXPLANATION);
        qr.setParam(word.getId());
        qr.runUpdate();
        qr.close();
        qr = QueryRunner.getRunner(Query.DELETE_WORD);
        qr.setParam(word.getId());
        qr.runUpdate();
        qr.close();
        LOG.info("Deleted word " + word.getWord() + " from system and db");
    }

    /**
     * Add a word into map
     *
     * @param word word object
     */
    public static void addWordToMap(Word word) {
        wordMap.put(word.getId(), word);
    }

    public static void removeWordFromMap(Word word) {
        wordMap.remove(word.getId());
    }

    /**
     * Fetch the id of the next word
     *
     * @return highest id in db incremented with one
     */
    public static int getNextId() {
        QueryRunner qr = QueryRunner.getRunner(Query.GET_MAX_WORD_ID);
        qr.run();
        try {
            return qr.getResults(Integer.class, "max").get(0) + 1;
        } catch (NullPointerException e) {
            return 1;
        }
    }

    private static Logger LOG = Logger.getLogger(WordProvider.class.getName());
}

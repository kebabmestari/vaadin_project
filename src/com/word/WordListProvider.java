package com.word;

import com.database.Query;
import com.database.QueryRunner;
import com.game.RoundInstance;
import com.user.User;

import java.util.*;
import java.util.logging.Logger;

/**
 * Several class methods to retvieve lists and list related data
 * Created by samlinz on 17.10.2016.
 */
public class WordListProvider {

    // list set
    private static Map<Integer, WordList> wordLists = new HashMap<>();

    /**
     * Return a wordlist or if it doesn't exists, compose it
     *
     * @return word list object
     */
    public static WordList getList(int id) throws Exception {
        WordList result = wordLists.get(id);
        if (result == null) {
            result = WordListBuilder.buildWordList(id);
            if (result == null) {
                throw new Exception("Word list " + id + " not found");
            }
        }
        return result;
    }

    /**
     * Retvieve all lists
     *
     * @return ArrayList of all WordList objects
     */
    public static List<WordList> getAllLists() {
        QueryRunner qr = QueryRunner.getRunner(Query.GET_ALL_LISTS);
        qr.run();
        List<Integer> results = qr.getResults(Integer.class, "idList");
        List<WordList> result = new ArrayList<>();
        if (results == null) {
            LOG.warning("No lists found!");
            return result;
        }
        results.forEach((e) -> {
            try {
                result.add(getList(e));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return result;
    }

    /**
     * Check if list exists in db
     *
     * @param name name of the list
     * @return true if exists
     */
    public static boolean listExists(String name) {
        name = name.toLowerCase();
        QueryRunner qr = QueryRunner.getRunner(Query.LIST_EXISTS_BY_NAME);
        qr.setParam(name);
        qr.run();
        return qr.getResults(String.class, "name").size() > 0;
    }

    /**
     * Append list to hashmap
     *
     * @param list list object
     */
    public static void addListToMap(WordList list) {
        wordLists.put(list.getId(), list);
    }

    /**
     * Get a list of words that belong to the list
     *
     * @param id list id
     * @return arraylist of word objects
     */
    static List<Word> getListWords(int id) {
        LOG.info("Fetching words for list " + id);
        List<Word> result = new ArrayList<>();
        QueryRunner qr = QueryRunner.getRunner(Query.GET_LIST_WORDS);
        qr.setParam(id);
        qr.run();
        List<Integer> idList = qr.getResults(Integer.class, "idWord");
        for (int i : idList) {
            Word newWord = WordProvider.getWord(i);
            result.add(newWord);
        }
        if (result.size() == 0) {
            LOG.warning("No words found for list " + id);
        }
        return result;
    }

    /**
     * Fetch user made lists
     *
     * @param user
     * @return set of word lists
     */
    public static List<WordList> getUserLists(User user) {
        List<WordList> result = new ArrayList<>();
        Iterator iter = wordLists.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            WordList w = ((WordList) e.getValue());
            if ((w.getCreator() == user)) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Fetch user favourited lists
     *
     * @param user
     * @return set of wordlists
     */
    public static Set<WordList> getUserFavourites(User user) {

        return null;
    }

    /**
     * Get a single game instance of a list
     *
     * @param list word list
     * @return word list instance object
     */
    public static RoundInstance getInstance(WordList list) {
        RoundInstance newInstance = new RoundInstance();
        newInstance.setMaster(list);
        return newInstance;
    }

    /**
     * Flush a new word list to database
     *
     * @param list
     */
    public static void flushToDatabase(WordList list) {
        QueryRunner qr = QueryRunner.getRunner(Query.CREATE_LIST);
        qr.setParam(list.getId());
        qr.setParam(list.getName());
        qr.setParam(list.getCreator().getId());
        qr.setParam(list.getMaxScore());
        qr.runUpdate();
        LOG.info("List " + list.getName() + " flushed to database... flushing entries");
        qr.close();
        int wordCount = 0;
        for (Word w : list.getWords()) {
            qr = QueryRunner.getRunner(Query.CREATE_WORDENTRY);
            qr.setParam(w.getId());
            qr.setParam(list.getId());
            qr.runUpdate();
            qr.close();
            wordCount++;
        }
        LOG.info("Flushed " + wordCount + " word entries to list " + list.getId() + " to db");
    }

    /**
     * Delete list
     *
     * @param list
     */
    public static void deleteList(WordList list) {
        for (Word w : list.getWords()) {
            QueryRunner qr = QueryRunner.getRunner(Query.DELETE_WORDENTRY);
            qr.setParam(list.getId());
            qr.runUpdate();
            qr.close();
        }
        QueryRunner qr = QueryRunner.getRunner(Query.DELETE_LIST);
        qr.setParam(list.getId());
        qr.runUpdate();
        qr.close();
        wordLists.remove(list.getId());
    }

    /**
     * Fetch the id of the next list
     *
     * @return highest id in db incremented with one
     */
    public static int getNextId() {
        QueryRunner qr = QueryRunner.getRunner(Query.GET_MAX_WORDLIST_ID);
        qr.run();
        List<Integer> max = qr.getResults(Integer.class, "max");
        try {
            if (max != null) {
                if (!max.isEmpty()) {
                    return max.get(0) + 1;
                }
            }
        } catch (NullPointerException e) {
        }
        return 1;
    }

    private static Logger LOG = Logger.getLogger(WordListProvider.class.getName());
}

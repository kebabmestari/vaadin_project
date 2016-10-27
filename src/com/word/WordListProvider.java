package com.word;

import com.database.Queries;
import com.database.Query;
import com.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;

/**
 * Created by samlinz on 17.10.2016.
 */
public class WordListProvider {

    // list set
    private static Set<WordList> wordLists;

    /**
     * Return a wordlist or if it doesn't exists, compose it
     * @return word list object
     */
    public static WordList getList(int id) {

    }

    public static void fetchList(int id) {
        final PreparedStatement query = Queries.getQuery(Query.GET_LIST);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            rs.getString("name");
        }
    }

    /**
     * Fetch user made lists
     * @param user
     * @return set of word lists
     */
    public static Set<WordList> getUserLists(User user) {

        return null;
    }

    /**
     * Fetch user favourited lists
     * @param user
     * @return set of wordlists
     */
    public static Set<WordList> getUserFavourites(User user) {

        return null;
    }
}

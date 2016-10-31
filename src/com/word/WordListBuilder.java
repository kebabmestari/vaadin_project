package com.word;

import com.database.Queries;
import com.database.Query;
import com.database.QueryRunner;
import com.game.Result;
import com.game.ResultFactory;
import com.user.UserProvider;
import com.word.lang.LanguageProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Builder class for word lists
 * Created by samlinz on 22.10.2016.
 */
public class WordListBuilder {
    /**
     * Builds a list
     * @param id word list id
     * @return WordList object
     */
    public static WordList buildWordList(int id) {
        LOG.info("Building word list " + id);
        final QueryRunner qr = QueryRunner.getRunner(Query.GET_LIST);
        qr.setParam(id);
        qr.run();
        // check if the list exists in the first place
        if(qr.getResultRows() <= 0) {
            LOG.warning("List " + id + " cannot be built. It doesn't exists in db!");
            return null;
        }
        String name   = qr.getResults(String.class, "name").get(0);
        int creator   = qr.getResults(Integer.class, "creator").get(0);
        int maxPoints = qr.getResults(Integer.class, "maxPoints").get(0);
        WordList newList = new WordList();
        newList.setName(name);
        newList.setId(id);
        newList.setMaxScore(maxPoints);
        newList.setCreator(UserProvider.getUser(creator));
        // retrieve the words of the list
        final List<Word> listWords = WordListProvider.getListWords(id);
        newList.setWords(listWords);
        LOG.info("Word list " + id + " built");
        WordListProvider.addListToMap(newList);
        return newList;
    }

    private static Logger LOG = Logger.getLogger(WordListBuilder.class.getName());
}

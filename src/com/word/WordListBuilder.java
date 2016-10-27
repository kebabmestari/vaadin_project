package com.word;

import com.database.Queries;
import com.database.Query;
import com.game.Result;
import com.game.ResultFactory;
import com.user.UserProvider;
import com.word.lang.LanguageProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Builder class for word lists
 * Created by samlinz on 22.10.2016.
 */
public class WordListBuilder {
    /**
     * Executes SQL queries and builds the word list
     * @param id word list id
     * @return WordList object
     */
    public static WordList buildWordList(int id) {
        WordList result = new WordList();
        result.setId(id);

        PreparedStatement pt, pt2;
        ResultSet rs, rs2;

        try {
            pt = Queries.getQuery(Query.GET_LIST);
            rs = pt.executeQuery();
            if(rs.next()) {
                result.setName(rs.getString("name"));
                result.setCreator(UserProvider.getUser(rs.getString("creator")));
            } else {
                LOG.warning("No list " + id + " exists");
                return null;
            }
            pt.close();
            pt2 = Queries.getQuery(Query.GET_LIST_WORDS);
            rs2 = pt2.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idWord");
                Word word = WordProvider.getWord(id);

            }
            pt.close();
        } catch(SQLException e) {
            LOG.warning("Could not build word list id " + id);
            return null;
        } finally {
            try {
                if (!pt.isClosed()) {
                    pt.close();
                }
                if (!pt2.isClosed()) {
                    pt2.close();
                }
                if (!rs.isClosed()) {
                    rs.close();
                }
                if (!rs2.isClosed()) {
                    rs2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Logger LOG = Logger.getLogger(WordListBuilder.class.getName());
}

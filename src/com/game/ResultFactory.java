package com.game;

import com.database.Queries;
import com.database.Query;
import com.user.User;
import com.util.DateProvider;
import com.word.WordList;
import com.word.WordListProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Factory class for Result object
 * Created by samlinz on 22.10.2016.
 */
public class ResultFactory {

    // logger
    private static Logger LOG = Logger.getLogger(ResultFactory.class.getName());

    /**
     * Create a new Result-object
     * @param user
     * @param list
     * @param score
     * @param time spent time in seconds
     */
    public static Result createNewResult(User user, WordList list, int score, int time) {
        Result newResult = new Result();
        newResult.setDate(DateProvider.getDate());
        newResult.setList(list);
        newResult.setMaxScore(list.getMaxScore());
        newResult.setTime(time);
        LOG.fine("Created new result " + newResult.toString());
        return newResult;
    }

    /**
     * Fetch user's results from db
     * and create objects and appends those to
     * the user's personal list
     * @param user user object
     */
    static void fetchUserResults(User user) throws SQLException {
        if(user == null)
            throw new NullPointerException("Null user given to fetchUserResults");
        PreparedStatement st = Queries.getQuery(Query.GET_USER_RESULTS);
        st.setString(1, user.getName());
        ResultSet res = st.executeQuery();
        while(res.next()) {
            Result newRes =
                    ResultFactory.createNewResult(user,
                    WordListProvider.getList(res.getInt("idList")),
                    res.getInt("score"),
                    res.getInt("time"));
            user.addResult(newRes);
        }
    }
}

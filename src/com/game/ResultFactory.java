package com.game;

import com.database.Query;
import com.database.QueryRunner;
import com.user.User;
import com.util.DateProvider;
import com.word.WordList;
import com.word.WordListProvider;

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
     *
     * @param user
     * @param list
     * @param score
     */
    public static Result createNewResult(User user, WordList list, int score) {
        Result newResult = new Result();
        newResult.setDate(DateProvider.getDate());
        newResult.setList(list);
        newResult.setUser(user);
        newResult.setScore(score);
        newResult.setMaxScore(list.getMaxScore());
//        newResult.setTime(time);
        LOG.info("Created new result " + newResult.toString());
        return newResult;
    }

    /**
     * Fetch user's results from db
     * and create objects and appends those to
     * the user's personal list
     *
     * @param user user object
     */
    static void fetchUserResults(User user) throws SQLException {
        if (user == null)
            throw new NullPointerException("Null user given to fetchUserResults");
        QueryRunner qr = QueryRunner.getRunner(Query.GET_USER_RESULTS);
        qr.setParam(user.getName());
        qr.run();
        for (int i = 0; i < qr.getResultRows(); i++) {
            Result newRes = null;
            try {
                newRes = ResultFactory.createNewResult(user,
                        WordListProvider.getList(qr.getResults(Integer.class, "idList").get(0)),
                        qr.getResults(Integer.class, "score").get(0));
            } catch (Exception e) {
                // list not found
                e.printStackTrace();
            }
            user.addResult(newRes);
        }
    }
}

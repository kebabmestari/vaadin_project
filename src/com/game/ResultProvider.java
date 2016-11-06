package com.game;

import com.database.Query;
import com.database.QueryRunner;
import com.util.DateProvider;

import java.util.logging.Logger;

/**
 * Created by samlinz on 4.11.2016.
 */
public class ResultProvider {

    /**
     * Flush result into db
     * @param result
     */
    public static void flush(Result result) {
        QueryRunner qr = QueryRunner.getRunner(Query.CREATE_RESULT);
        qr.setParam(result.getDate());
        qr.setParam(result.getUser().getId());
        qr.setParam(result.getScore());
        qr.setParam(result.getList().getId());
        if(qr.runUpdate() > 0 ){
            LOG.info("Flushed result " + result.getUser().getName() + "-" + result.getList().getName() + "-" + result.getDate().toString());
        }
    }

    private static Logger LOG = Logger.getLogger(ResultProvider.class.getName());
}

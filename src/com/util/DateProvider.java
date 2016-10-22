package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Get date corresponding to the system format
 * Created by samlinz on 22.10.2016.
 */
public class DateProvider {

    // format used by the system
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // datetime format
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // logger
    private static Logger LOG = Logger.getLogger(DateProvider.class.getName());

    /**
     * @return Date object
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * @return current date as text corresponding to the format used by database
     */
    public static String getDateAsText() {
        return dateFormat.format(dateFormat.format(getDate()));
    }

     /**
     * @return current datetime as text corresponding to the format used by database
     */
    public static String getDateTimeAsText() {
        return dateTimeFormat.format(dateTimeFormat.format(getDate()));
    }

    static {
        LOG.fine("Date provider utility initialized");
    }
}

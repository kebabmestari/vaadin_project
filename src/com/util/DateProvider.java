package com.util;

import java.text.DateFormat;
import java.text.ParseException;
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
        return dateFormat.format(getDate());
    }

    /**
     * @return given date as text corresponding to the format used by database
     */
    public static String getDateAsText(Date date) {
        return dateFormat.format(dateFormat.format(date));
    }

    /**
     * @return current datetime as text corresponding to the format used by database
     */
    public static String getDateTimeAsText() {
        return dateTimeFormat.format(getDate());
    }

    /**
     * @return given datetime as text corresponding to the format used by database
     */
    public static String getDateTimeAsText(Date date) {
        return dateTimeFormat.format(dateTimeFormat.format(date));
    }

    /**
     * @param date date formatted as string
     * @return parsed date as Date object
     */
    public static Date getDateAsDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            LOG.warning("Invalid date string given to getDateAsDate");
        }
        return null;
    }

    /**
     * @param datetime datetime formatted as string
     * @return parsed date as Date object
     */
    public static Date getDateTimeAsDate(String datetime) {
        try {
            return dateTimeFormat.parse(datetime);
        } catch (ParseException e) {
            LOG.warning("Invalid datetime string given to getDateTimeAsDate");
        }
        return null;
    }

    static {
        LOG.info("Date provider utility initialized");
    }
}

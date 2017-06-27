package com.bobnono.popularmovies.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2017-06-26.
 */

public class DateUtils {
    public static Calendar stringToCalendar(String dateTimeString, String format){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(dateTimeString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  cal;
    }

    public static String calendarToString(Calendar cal, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        return sdf.format(cal.getTime());
    }

}

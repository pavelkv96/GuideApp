package com.grsu.guideapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converter {

    private static final String PATTERN = "dd-MM-yyyy_HH:mm:ss";

    public static Date toDate(String dateString) {
        Date date;
        try {
            date = new SimpleDateFormat(PATTERN, Locale.US).parse(dateString);
        } catch (ParseException e) {
            date = new Date(0);
        }
        return date;
    }

    public static String toStringDate(Object date) {
        return new SimpleDateFormat(PATTERN, Locale.US).format(date);
    }
}

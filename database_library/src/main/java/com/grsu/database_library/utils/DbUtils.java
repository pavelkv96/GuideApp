package com.grsu.database_library.utils;

public class DbUtils {

    public static String getTableName(final Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String set(String value) {
        return "'" + value + "'";
    }
    public static String set1(String value) {
        return "('" + value + "')";
    }
}

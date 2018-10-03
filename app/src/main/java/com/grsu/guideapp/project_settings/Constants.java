package com.grsu.guideapp.project_settings;

import com.grsu.guideapp.BuildConfig;

public class Constants {

    public static final Double ONE_METER_LAT = 0.000009;//1m ~ 0.000009
    public static final Double ONE_METER_LNG = 0.000015;//1m ~ 0.000015

    public static final String EMPTY_STRING = "";
    public static final String PACKAGE = "package";
    public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    public static final String ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS";

    //Key constants
    public static final String KEY_CHECKED_ITEM = "checked_item";
    public static final String KEY_SELECTED_ITEM = "selected_item";
    public static final String KEY_ID_ROUTE = "id_route";
    public final static String KEY_GEO_POINT = "geo_point";
    public static final String KEY_ID_POINT = "id_point";
    public static final String KEY_RECORD = "record";
    public static final String KEY_NAME_RECORD = "name_record";
    public static final String KEY_NAME_PLACE_RECORD = "name_place_record";

    //Notification action keys and chanel
    public static final String NOTIFY_PREVIOUS = "NOTIFY_PREVIOUS";
    public static final String NOTIFY_DELETE = "NOTIFY_DELETE";
    public static final String NOTIFY_PAUSE = "NOTIFY_PAUSE";
    public static final String NOTIFY_PLAY = "NOTIFY_PLAY";
    public static final String NOTIFY_NEXT = "NOTIFY_NEXT";
    public static final int NOTIFICATION_ID = 9;

    public static final String CHANNEL_ID = "exampleServiceChannel";

    //Type content
    public static final String JPG = ".jpg";
    public static final String MP3 = ".mp3";
}

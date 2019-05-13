package com.grsu.guideapp.project_settings;

import com.grsu.guideapp.BuildConfig;

public class Constants {

    public enum Language {
        en, ru, zh, pl, lt
    }

    public static final String EMPTY_STRING = "";
    public static final String PACKAGE = "package";
    public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    public static final String ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS";

    //Key constants
    public static final String KEY_BUNDLE_ROUTE = "bundle_route";
    public static final String KEY_NAME_ROUTE = "name_route";
    public static final String KEY_SOUTHWEST = "southwest";
    public static final String KEY_NORTHEAST = "northeast";
    public static final String KEY_IS_FULL = "is_full";
    public static final String KEY_CHECKED_ITEM = "checked_item";
    public static final String KEY_SELECTED_ITEM = "selected_item";
    public static final String KEY_ID_ROUTE = "id_route";
    public static final String KEY_GEO_POINT = "geo_point";
    public static final String KEY_ID_POINT = "id_point";
    public static final String KEY_RECORD = "record";
    public static final String KEY_NAME_RECORD = "name_record";
    public static final String KEY_NAME_PLACE_RECORD = "name_place_record";
    public static final String KEY_MULTI_CHOICE_ITEMS = "multi_choice_items";
    public static final String KEY_SINGLE_CHOICE_ITEM = "single_choice_item";

    //Notification action keys and chanel
    public static final String NOTIFY_PREVIOUS = "NOTIFY_PREVIOUS";
    public static final String NOTIFY_DELETE = "NOTIFY_DELETE";
    public static final String NOTIFY_PAUSE = "NOTIFY_PAUSE";
    public static final String NOTIFY_PLAY = "NOTIFY_PLAY";
    public static final String NOTIFY_NEXT = "NOTIFY_NEXT";
    public static final int NOTIFICATION_ID = 9;

    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static final String DATABASES = "databases";

    //Type content
    public static final String JPG = "jpg";
    public static final String MP3 = "mp3";
}

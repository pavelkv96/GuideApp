package com.grsu.guideapp.project_settings;

import com.grsu.guideapp.utils.StorageUtils;

public class Settings {

    private static final String DEFAULT_PATH = StorageUtils.getStorage() + "/";
    private static final String APP_FOLDER = "osmdroid/";

    //-------------------------------------------
    //	Summary: settings map database
    //-------------------------------------------

    public static final Integer CACHE_DATABASE_VERSION = 1;
    public static final String CACHE_DATABASE_NAME = "cache.db";
    private static final String CACHE_FOLDER = "cache/";
    public static final String CACHE = DEFAULT_PATH + APP_FOLDER + CACHE_FOLDER;


    //-------------------------------------------
    //	Summary: settings content database
    //-------------------------------------------

    public static final Integer DATABASE_INFORMATION_VERSION = 1;
    public static final String DATABASE_INFORMATION_NAME = "GuideApp_v7_2.db";
    public static final String DATABASE_JOURNAL = DATABASE_INFORMATION_NAME + "-journal";


    //-------------------------------------------
    //	Summary: settings tile provider
    //-------------------------------------------

    private static final String PROVIDER_MAPSFORGE = "Mapsforge";
    public static final String CURRENT_PROVIDER = PROVIDER_MAPSFORGE;


    //-------------------------------------------
    //	Summary: settings photo and audio folder
    //-------------------------------------------

    private static final String PHOTO_FOLDER = "photo/";
    private static final String AUDIO_FOLDER = "audio/";
    private static final String CONTENT_FOLDER = "content/";
    public static final String CONTENT = DEFAULT_PATH + APP_FOLDER + CONTENT_FOLDER;
    public static final String PHOTO_CONTENT = CONTENT + PHOTO_FOLDER;
    public static final String AUDIO_CONTENT = CONTENT + AUDIO_FOLDER;


    //-------------------------------------------
    //	Summary: settings names important file
    //-------------------------------------------

    public static final String ZOOM_TABLE = "ZoomTables.data";
    public static final String MAP_FILE = "KA.map";
    public static final String THEME_FOLDR = "renderthemes/";
    public static final String THEME_FILE = "osmarender.xml";

    //-------------------------------------------
    //	Summary: map settings
    //-------------------------------------------

    public static final float MIN_ZOOM_LEVEL = 13;
    public static final float MAX_ZOOM_LEVEL = 18;
}

package com.grsu.guideapp.project_settings;

import com.grsu.guideapp.utils.StorageUtils;

public class Settings {

    private static final String DEFAULT_PATH = StorageUtils.getStorage() + "/";
    private static final String APP_FOLDER = "cache/";

    //-------------------------------------------
    //	Summary: settings content database
    //-------------------------------------------

    public static final Integer DATABASE_INFORMATION_VERSION = 1;
    public static final String DATABASE_INFORMATION_NAME = "GuideApp_v9_1.db";

    //-------------------------------------------
    //	Summary: settings photo and audio folder
    //-------------------------------------------

    private static final String CONTENT_FOLDER = "content/";
    public static final String CONTENT = DEFAULT_PATH + APP_FOLDER + CONTENT_FOLDER;


    //-------------------------------------------
    //	Summary: settings names important file
    //-------------------------------------------

    public static final String ZOOM_TABLE = "ZoomTables.data";
    public static final String MAP_FILE = "KA.map";

    //-------------------------------------------
    //	Summary: map settings
    //-------------------------------------------

    public static final float MIN_ZOOM_LEVEL = 10;
    public static final float MAX_ZOOM_LEVEL = 20;
}

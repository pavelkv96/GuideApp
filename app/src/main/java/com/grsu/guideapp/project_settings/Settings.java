package com.grsu.guideapp.project_settings;

import com.grsu.guideapp.utils.StorageUtils;

public class Settings {

    //-------------------------------------------
    //	Summary: settings map database
    //-------------------------------------------

    public static final Integer CACHE_DATABASE_VERSION = 1;
    public static final String CACHE_DATABASE_NAME = "cache.db";
    public static final String CURRENT_FOLDER = "osmdroid/";
    public static final String CACHE_FOLDER = "cache/";
    public static final String CACHE = StorageUtils.getStorage() + "/" + CURRENT_FOLDER + CACHE_FOLDER;


    //-------------------------------------------
    //	Summary: settings content database
    //-------------------------------------------

    public static final Integer DATABASE_INFORMATION_VERSION = 1;
    public static final String DATABASE_INFORMATION_NAME = "GuideApp_v5.db";


    //-------------------------------------------
    //	Summary: settings tile provider
    //-------------------------------------------

    private static final String PROVIDER_MAPSFORGE = "Mapsforge";
    public static final String CURRENT_PROVIDER = PROVIDER_MAPSFORGE;
}

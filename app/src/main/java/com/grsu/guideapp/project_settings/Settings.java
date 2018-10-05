package com.grsu.guideapp.project_settings;

import com.google.android.gms.maps.model.LatLng;
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
    //	Summary: settings tile provider
    //-------------------------------------------

    private static final String PROVIDER_MAPSFORGE = "Mapsforge";
    public static final String CURRENT_PROVIDER = PROVIDER_MAPSFORGE;

    //-------------------------------------------
    //	Summary: settings names important file
    //-------------------------------------------

    public static final String ZOOM_TABLE = "ZoomTables.data";
    public static final String MAP_FILE = "KA.map";

    //-------------------------------------------
    //	Summary: map settings
    //-------------------------------------------

    public static final float MIN_ZOOM_LEVEL = 13;
    public static final float MAX_ZOOM_LEVEL = 19;
    public static final LatLng NORTH_WEST = new LatLng(53.5986, 23.7099);
    public static final LatLng SOUTH_EAST = new LatLng(53.7597, 23.9845);
}

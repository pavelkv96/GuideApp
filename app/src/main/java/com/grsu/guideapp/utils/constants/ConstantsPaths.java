package com.grsu.guideapp.utils.constants;

import com.grsu.guideapp.utils.StorageUtils;

public interface ConstantsPaths {

    String CACHE_FILE = "cache.db";
    String CURRENT_FOLDER = "osmdroid/";
    String CACHE_FOLDER = "cache/";
    String CACHE = StorageUtils.getStorage() + "/" + CURRENT_FOLDER + CACHE_FOLDER;
}

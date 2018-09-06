package com.grsu.guideapp.project_settings.constants;

import com.grsu.guideapp.utils.StorageUtils;

public interface ConstantsPaths {

    String CACHE_FILE = "cache.db";
    String CURRENT_FOLDER = "osmdroid/";
    String CACHE_FOLDER = "cache/";
    String CACHE = StorageUtils.getStorage() + "/" + CURRENT_FOLDER + CACHE_FOLDER;
}

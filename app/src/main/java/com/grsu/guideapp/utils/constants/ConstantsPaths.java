package com.grsu.guideapp.utils.constants;

import com.grsu.guideapp.utils.StorageUtils;

public interface ConstantsPaths {

    String CACHE_FILE = "cache.db";
    String DEFAULT_FOLDER = "osmdroid/cache/";
    String CACHE = StorageUtils.getStorage() + "/" + DEFAULT_FOLDER;

}

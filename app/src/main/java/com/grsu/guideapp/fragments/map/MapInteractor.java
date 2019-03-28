package com.grsu.guideapp.fragments.map;

import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewInteractor;

public class MapInteractor extends MapPreviewInteractor implements MapContract.MapsInteractor {

    public MapInteractor(@NonNull DatabaseHelper pDbHelper) {
        super(pDbHelper);
    }
}

package com.grsu.guideapp.fragments.map;

import androidx.annotation.NonNull;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.map_preview.MapPreviewInteractor;

public class MapInteractor extends MapPreviewInteractor implements MapContract.MapsInteractor {

    public MapInteractor(@NonNull Test pDbHelper) {
        super(pDbHelper);
    }
}

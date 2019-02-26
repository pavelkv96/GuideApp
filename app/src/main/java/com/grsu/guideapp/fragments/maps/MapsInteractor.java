package com.grsu.guideapp.fragments.maps;

import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewInteractor;

public class MapsInteractor extends MapPreviewInteractor implements MapsContract.MapsInteractor {

    public MapsInteractor(@NonNull DatabaseHelper pDbHelper) {
        super(pDbHelper);
    }
}

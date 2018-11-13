package com.grsu.guideapp.fragments.map;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewInteractor;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public class MapInteractor extends MapPreviewInteractor implements MapContract.MapInteractor {

    public MapInteractor(@NonNull DatabaseHelper pDbHelper) {
        super(pDbHelper);
    }

    @Override
    public void getListPoi(final OnFinishedListener<List<Poi>> listener, final double latitude,
            final double longitude, final int radius, final long[] typesObjects) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getListPoi(latitude, longitude, radius, typesObjects));
            }
        }/*, 3000*/);
    }
}

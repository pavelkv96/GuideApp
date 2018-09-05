package com.grsu.guideapp.fragments.test;

import static com.grsu.guideapp.mf.MapsForgeTileSource.loadTile;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.utils.MapUtils;
import java.util.List;

public class TestInteractor implements TestContract.TestInteractor {

    private DatabaseHelper helper;

    public TestInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public Tile getTile(OnFinishedListener listener, long index, String provider) {
        return listener.onFinished(loadTile(index));
    }

    @Override
    public void getRouteById(final OnFinishedListener listener, final Integer routeId) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getRouteById(routeId));
            }
        }/*, 3000*/);
    }

    @Override
    public void getListPoi(final OnFinishedListener listener, final double latitude,
            final double longitude, final int radius, final List<Integer> typesObjects) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished1(helper.getListPoi(latitude, longitude, radius, typesObjects));
            }
        }/*, 3000*/);
    }
}

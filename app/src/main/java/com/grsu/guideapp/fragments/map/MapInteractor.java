package com.grsu.guideapp.fragments.map;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;
import java.util.List;

public class MapInteractor implements MapContract.MapInteractor {

    private DatabaseHelper helper;

    public MapInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
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
    public void getListPoi(final OnFinishedListener listener, final double latitude, final double longitude,
            final int radius, final List<Integer> typesObjects) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished1(helper.getListPoi(latitude, longitude, radius, typesObjects));
            }
        }/*, 3000*/);
    }
}

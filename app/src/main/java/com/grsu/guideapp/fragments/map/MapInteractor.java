package com.grsu.guideapp.fragments.map;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

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
}

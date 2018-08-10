package com.grsu.guideapp.activities.route;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class RouteInteractor implements RouteContract.RouteInteractor {

    private DatabaseHelper helper;

    public RouteInteractor(@NonNull DatabaseHelper pDbHelper) {
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
    public void getListPoi(final OnFinishedListener listener, final double a, final double b,
            final int c, final List<Integer> typesObjects) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished1(helper.getListPoi(a, b, c, typesObjects));
            }
        }/*, 3000*/);
    }

    @Override
    public void getPoiById(final OnFinishedListener listener, final GeoPoint geoPoint) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished1(helper.getPoiById(geoPoint));
            }
        }/*, 3000*/);
    }
}

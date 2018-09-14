package com.grsu.guideapp.activities.route;

import static com.grsu.guideapp.mf.MapsForgeTileSource.loadTile;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnFinishedTileListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public class RouteInteractor implements RouteContract.RouteInteractor {

    private DatabaseHelper helper;

    public RouteInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public Tile getTile(OnFinishedTileListener<Tile> listener, long index, String provider) {
        return listener.onFinished(loadTile(index));
    }

    @Override
    public void getRouteById(final OnFinishedListener<List<Line>> listener, final Integer routeId) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getRouteById(routeId));
            }
        }/*, 3000*/);
    }

    @Override
    public void getListPoi(final OnFinishedListener<List<Poi>> listener, final double latitude,
            final double longitude, final int radius, final List<Integer> typesObjects) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getListPoi(latitude, longitude, radius, typesObjects));
            }
        }/*, 3000*/);
    }

    @Override
    public void getListPoi(final OnFinishedListener<List<Poi>> listener, final Integer id,
            final List<Integer> typesObjects) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getListPoi(id, typesObjects));
            }
        }/*, 3000*/);
    }
}

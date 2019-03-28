package com.grsu.guideapp.fragments.map_preview_v1;

import android.os.Handler;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public class MapPreviewInteractor implements MapPreviewContract.MapPreviewInteractor {

    protected DatabaseHelper helper;

    public MapPreviewInteractor(DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public void getRouteById(final OnFinishedListener<List<Line>> listener, final Integer routeId) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getRouteById(routeId));
            }
        });
    }

    @Override
    public void getListPoi(final OnFinishedListener<List<Poi>> listener, final Integer id,
            final String point, final int radius) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getListPoi(id, point, radius));
            }
        });
    }

    @Override
    public void getCountCheckedTypes(final OnFinishedListener<Integer> listener) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getCountCheckedTypes());
            }
        });
    }
}

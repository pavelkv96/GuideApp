package com.grsu.guideapp.activities.route;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;

public class RouteInteractor implements RouteContract.RouteInteractor {

    private DatabaseHelper helper;

    public RouteInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public void getRouteById(final OnFinishedListener listener, final Integer routeId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getRouteById(routeId));
            }
        }, 3000);
    }
}

package com.grsu.guideapp.fragments.test;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;

public class TestInteractor implements TestContract.TestInteractor {

    private DatabaseHelper helper;

    public TestInteractor(@NonNull DatabaseHelper pDbHelper) {
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

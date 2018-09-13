package com.grsu.guideapp.activities.details;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;

public class DetailsInteractor implements DetailsContract.DetailsInteractor {

    private DatabaseHelper helper;

    public DetailsInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public void getInfoById(final OnFinishedListener listener, final String id) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getInfoById(id));
            }
        }/*, 3000*/);
    }
}
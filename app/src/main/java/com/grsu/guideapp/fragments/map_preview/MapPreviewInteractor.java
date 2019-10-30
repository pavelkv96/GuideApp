package com.grsu.guideapp.fragments.map_preview;

import android.os.Handler;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.models.DtoObject;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public class MapPreviewInteractor implements MapPreviewContract.MapPreviewInteractor {

    protected Test helper;

    public MapPreviewInteractor(Test pDbHelper) {
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

    @Override
    public void getObjectInfo(final OnSuccessListener<DtoObject> listener, final int id, final String locale) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(helper.getObjectById(id, locale).get(0));
                }catch (Exception e){
                    listener.onFailure(e);
                }
            }
        });
    }
}

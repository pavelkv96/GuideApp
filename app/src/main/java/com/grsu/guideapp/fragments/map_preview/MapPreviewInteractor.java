package com.grsu.guideapp.fragments.map_preview;

import com.grsu.guideapp.App;
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
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                final List<Line> routeById = helper.getRouteById(routeId);
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinished(routeById);
                    }
                });
            }
        });
    }

    @Override
    public void getListPoi(final OnFinishedListener<List<Poi>> listener, final Integer id,
                           final String point, final int radius) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                final List<Poi> listPoi = helper.getListPoi(id, point, radius);
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinished(listPoi);
                    }
                });
            }
        });
    }

    @Override
    public void getCountCheckedTypes(final OnFinishedListener<Integer> listener) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                final int countCheckedTypes = helper.getCountCheckedTypes();
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinished(countCheckedTypes);
                    }
                });
            }
        });
    }

    @Override
    public void getObjectInfo(final OnSuccessListener<DtoObject> listener, final int id, final String locale) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    final DtoObject task = helper.getObjectById(id, locale).get(0);
                    App.getThread().mainThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(task);
                        }
                    });
                } catch (final Exception e) {
                    App.getThread().mainThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(e);
                        }
                    });
                }
            }
        });
    }
}

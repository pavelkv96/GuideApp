package com.grsu.guideapp.fragments.open_route;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.open_route.RoutePreviewContract.RouteInteractor;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public class RoutePreviewInteractor implements RouteInteractor {

    private static final String TAG = RoutePreviewInteractor.class.getSimpleName();

    private Test mHelper;

    RoutePreviewInteractor(Test pHelper) {
        mHelper = pHelper;
    }

    @Override
    public void getRouteById(final OnSuccessListener<DtoRoute> listener, final int id) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getRoute(id, "en"));
                }catch (Exception e){
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void getLines(final OnSuccessListener<List<Line>> listener, final int id) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getRouteById((id)));
                }catch (Exception e){
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void getPoi(final OnSuccessListener<List<Poi>> listener, final int id) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getListPoi(id, null, 1000));
                }catch (Exception e){
                    listener.onFailure(e);
                }
            }
        });
    }
}

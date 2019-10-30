package com.grsu.guideapp.fragments.open_route;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.open_route.RoutePreviewContract.RouteInteractor;
import com.grsu.guideapp.fragments.open_route.RoutePreviewContract.RoutePresenter;
import com.grsu.guideapp.fragments.open_route.RoutePreviewContract.RouteViews;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public class RoutePreviewPresenter extends BasePresenterImpl<RouteViews>
        implements RoutePresenter {

    private RouteInteractor mInteractor;

    RoutePreviewPresenter(RouteInteractor pInteractor) {
        mInteractor = pInteractor;
    }

    private static final String TAG = RoutePreviewPresenter.class.getSimpleName();

    @Override
    public void getRouteById(int id) {
        mInteractor.getRouteById(new OnSuccessListener<DtoRoute>() {
            @Override
            public void onSuccess(final DtoRoute route) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.setBounds(route);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        }, id);
    }

    @Override
    public void getLines(int id) {
        mInteractor.getLines(new OnSuccessListener<List<Line>>() {
            @Override
            public void onSuccess(final List<Line> lines) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.setLines(lines);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        }, id);
    }

    @Override
    public void getPoi(int id) {
        mInteractor.getPoi(new OnSuccessListener<List<Poi>>() {
            @Override
            public void onSuccess(final List<Poi> poi) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.setPoi(poi);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        }, id);
    }
}

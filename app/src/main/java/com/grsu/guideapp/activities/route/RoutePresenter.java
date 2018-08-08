package com.grsu.guideapp.activities.route;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.models.Line;
import java.util.List;

public class RoutePresenter extends BasePresenterImpl<RouteView> implements OnFinishedListener,
        RouteContract.RoutePresenter {

    private RouteView routeView;
    private RouteInteractor routeInteractor;

    public RoutePresenter(RouteView routeView, RouteInteractor routeInteractor) {
        this.routeView = routeView;
        this.routeInteractor = routeInteractor;
    }

    @Override
    public void getID(Integer id) {
        mView.showProgress(null, "Loading");
        routeInteractor.getRouteById(this, id);
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        Line line = null;
        for (Line encodePolyline : encodePolylines) {
            routeView.setPolyLine(decodeL(encodePolyline.getPolyline()));
            routeView.setPoints(decodeP(encodePolyline.getStartPoint()));
            line = encodePolyline;
        }
        routeView.setPoints(decodeP(line.getEndPoint()));
        mView.hideProgress();
    }
}
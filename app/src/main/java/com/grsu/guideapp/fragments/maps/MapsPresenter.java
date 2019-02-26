package com.grsu.guideapp.fragments.maps;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.base.listeners.OnChangePolyline;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnNotFound;
import com.grsu.guideapp.fragments.maps.MapsContract.MapViews;
import com.grsu.guideapp.fragments.map_preview.MapPreviewPresenter;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MapUtils;
import java.util.List;

public class MapsPresenter extends MapPreviewPresenter implements MapsContract.MapsPresenter,
        OnChangePolyline, OnNotFound {

    private MapViews mapViews;
    private MapsInteractor mapInteractor;
    private Logic logic;
    private boolean flag;

    public MapsPresenter(MapViews mapViews, MapsInteractor mapInteractor) {
        super(mapViews, mapInteractor);
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
        this.logic = Logic.getInstance(this);
    }

    private static final Integer RADIUS = 100;

    @Override
    public void getId(Integer id) {
        super.getId(id);
        mapInteractor.getRouteById(new OnFinishedListener<List<Line>>() {
            @Override
            public void onFinished(List<Line> lines) {
                logic.initialData(lines);
            }
        }, id);
    }

    @Override
    public void getProjectionLocation(Location currentLocation) {
        Point point = logic.findNearestPointInPolyline(currentLocation);

        //mapViews.setCurrentPoint(point.getPosition());

        getCurrentTurn(MapUtils.toLocation(point.getPosition()));
    }

    @Override
    public void getAllPoi(boolean getAll) {
        super.getAllPoi(getAll);
        flag = getAll;
        if (!flag) {
            getPoi();
        }
    }

    @Override
    public void getPoi() {
        if (logic.getCurrentPosition().getPosition() != null) {
            getCurrentTurn(MapUtils.toLocation(logic.getCurrentPosition().getPosition()));
        }
    }

    private void getCurrentTurn(final Location currentLocation) {
        if (flag) {
            return;
        }
        Point point = logic.getShortestDistance(logic.getTurnsList(), currentLocation);
        final LatLng shortestDistance = point.getPosition();
        mapInteractor.getCountCheckedTypes(new OnFinishedListener<Integer>() {
            @Override
            public void onFinished(Integer count) {
                if (MapUtils.isMoreDistance(RADIUS, currentLocation, shortestDistance)
                        && count != 0) {
                    String point = CryptoUtils.encodeP(shortestDistance);
                    mapInteractor.getListPoi(MapsPresenter.this, null, point, radius);
                } else {
                    mapViews.removePoi();
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        logic.detachLogic();
    }

    @Override
    public void onChange(Integer previous, Integer current) {
        mapViews.showToast("Previous polyline: " + previous + ";\nCurrent: " + current);
    }

    @Override
    public void onNotFound(Float distance) {
        if (distance != null) {
            mapViews.show();
        } else {
            mapViews.hide();
        }
    }

    public List<Point> getList(Location currentLocation) {
        getProjectionLocation(currentLocation);

        return logic.getList();
    }
}

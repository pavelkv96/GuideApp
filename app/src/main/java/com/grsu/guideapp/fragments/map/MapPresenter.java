package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.base.listeners.OnChangePolyline;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnNotFound;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.fragments.map_preview.MapPreviewPresenter;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.utils.MapUtils;
import java.util.List;

public class MapPresenter extends MapPreviewPresenter implements MapContract.MapPresenter,
        OnChangePolyline, OnNotFound {

    private MapViews mapViews;
    private MapInteractor mapInteractor;
    private Logic logic;
    private boolean flag;

    public MapPresenter(MapViews mapViews, MapInteractor mapInteractor) {
        super(mapViews, mapInteractor);
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
        this.logic = Logic.getInstance(this);
    }

    private static final Integer RADIUS = 100;
    private static final String TAG = MapPresenter.class.getSimpleName();

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

        mapViews.setCurrentPoint(point.getPosition());

        getCurrentTurn(MapUtils.toLocation(logic.getCurrentPosition().getPosition())/*, true*/);
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

    private void getCurrentTurn(Location currentLocation) {
        if (flag || types == null) {
            return;
        }

        LatLng shortestDistance = logic.getShortestDistance(logic.getTurnsList(), currentLocation)
                .getPosition();
        int size = types.length;


        if (MapUtils.isMoreDistance(RADIUS, currentLocation, shortestDistance) && size != 0) {

            mapInteractor.getListPoi(
                    this,
                    shortestDistance.latitude,
                    shortestDistance.longitude,
                    checkedItem,
                    types);
        } else {
            mapViews.removePoi();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        logic.detachLogic();
    }

    @Override
    public void onChange(Integer previous, Integer current) {
        mapViews.showT("Previous polyline: " + previous + ";\nCurrent: " + current);
    }

    @Override
    public void onNotFound(Float distance) {
        if (distance != null) {
            mapViews.show();
        } else {
            mapViews.hide();
        }
    }
}

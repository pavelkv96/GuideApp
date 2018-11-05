package com.grsu.guideapp.fragments.map;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnChangePolyline;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.details.DetailsFragment;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.DecodeLine;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.models.Tag;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.List;

public class MapPresenter extends BasePresenterImpl<MapViews> implements MapContract.MapPresenter,
        OnFinishedListener<List<Poi>>, OnChangePolyline {

    private MapViews mapViews;
    private MapInteractor mapInteractor;
    private Logic logic;

    public MapPresenter(MapViews mapViews, MapInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
        this.logic = Logic.getInstance(this);
    }

    private static final Integer RADIUS = 100;
    private static final String TAG = MapPresenter.class.getSimpleName();

    private long[] types;
    private Integer checkedItem;
    private boolean flag;
    private Integer id;

    @Override
    public void getId(Integer id) {
        mView.showProgress(null, "Loading...");
        mapInteractor.getRouteById(new OnFinishedListener<List<Line>>() {
            @Override
            public void onFinished(List<Line> encodePolylines) {
                logic.initialData(encodePolylines);

                List<Point> turnsList = logic.getTurnsList();
                int size = turnsList.size();

                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        mapViews.setStartMarker(turnsList.get(i).getPosition());
                    } else {
                        if (i == size - 1) {
                            mapViews.setEndMarker(turnsList.get(i).getPosition());
                        } else {
                            mapViews.setPointsTurn(turnsList.get(i).getPosition());
                        }
                    }

                }

                for (DecodeLine line : logic.getDecodeLines()) {
                    mapViews.setPolyline(line.getPolyline(), line.getIdLine());
                }
                mView.hideProgress();
            }
        }, id);
        this.id = id;
    }

    @Override
    public void getProjectionLocation(Location currentLocation) {
        Point point = logic.findNearestPointInPolyline(currentLocation);

        mapViews.setCurrentPoint(point.getPosition());

        getCurrentTurn(MapUtils.toLocation(logic.getCurrentPosition().getPosition()));
    }

    @Override
    public void setRadius(Integer radius) {
        checkedItem = radius;
    }

    @Override
    public void setType(long[] typesObjects) {
        types = typesObjects;
    }

    @Override
    public long[] getType() {
        return types;
    }

    @Override
    public void setAllPoi(boolean getAll) {
        flag = getAll;
        getAllPoi();
    }

    @Override
    public void getAllPoi() {
        if (getType() == null) {
            return;
        }

        int size = getType().length;
        if (flag && size != 0) {
            mapInteractor.getListPoi(this, id, getType());
        } else {
            getPoi();
        }
    }

    @Override
    public void getPoi() {
        if (logic.getCurrentPosition().getPosition() != null) {
            getCurrentTurn(MapUtils.toLocation(logic.getCurrentPosition().getPosition()));
        }
    }

    @Override
    public void onMarkerClick(Context context, Marker marker) {
        Tag tag = (Tag) marker.getTag();
        if (tag == null) {
            return;
        }

        if (tag.isPoi()) {
            ((RouteActivity) context).onReplace(DetailsFragment.newInstance(tag.getId()));
        }
    }

    @Override
    public void onFinished(List<Poi> poiList) {
        mapViews.removePoi();
        for (Poi poi : poiList) {
            mapViews.setPoi(poi);
        }
    }

    private void getCurrentTurn(Location currentLocation) {
        if (flag || getType() == null) {
            return;
        }

        LatLng shortestDistance = logic.getShortestDistance(logic.getTurnsList(), currentLocation)
                .getPosition();
        int size = getType().length;

        if (MapUtils.isMoreDistance(RADIUS, currentLocation, shortestDistance) && size != 0) {

            mapInteractor.getListPoi(
                    this,
                    shortestDistance.latitude,
                    shortestDistance.longitude,
                    checkedItem,
                    getType());
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
        Logs.e(TAG, "Previous: " + previous + "; Current " + current);
    }
}

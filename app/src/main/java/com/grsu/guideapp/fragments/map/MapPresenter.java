package com.grsu.guideapp.fragments.map;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.details.DetailsFragment;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Tag;
import com.grsu.guideapp.utils.MapUtils;
import java.util.List;

public class MapPresenter extends BasePresenterImpl<MapViews> implements MapContract.MapPresenter,
        OnFinishedListener<List<Poi>> {

    private MapViews mapViews;
    private MapInteractor mapInteractor;
    private Logic logic;

    public MapPresenter(MapViews mapViews, MapInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
        this.logic = Logic.getInstance();
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

                int size = logic.getTurnsList().size();

                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        mapViews.setStartMarker(logic.getTurnsList().get(i));
                    } else {
                        if (i == size - 1) {
                            mapViews.setEndMarker(logic.getTurnsList().get(i));
                        } else {
                            mapViews.setPointsTurn(logic.getTurnsList().get(i));
                        }
                    }

                }

                mapViews.setPolyline(logic.getAllLatLngs(), 0);
                mView.hideProgress();
            }
        }, id);
        this.id = id;
    }

    @Override
    public void getProjectionLocation(Location currentLocation) {
        LatLng point = logic.findNearestPointInPolyline(currentLocation);

        mapViews.setCurrentPoint(point);

        logic.setCurrentIndex(point);

        logic.detach(logic.getLatLngs(point));

        getCurrentTurn(MapUtils.toLocation(logic.getCurrentLatLng()));
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
        if (logic.getCurrentLatLng() != null) {
            getCurrentTurn(MapUtils.toLocation(logic.getCurrentLatLng()));
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

        LatLng shortestDistance = logic.getShortestDistance(logic.getTurnsList(), currentLocation);
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
        Logic.detachLogic();
    }
}

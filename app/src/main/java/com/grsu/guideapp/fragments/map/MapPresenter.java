package com.grsu.guideapp.fragments.map;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.map.MapContract.MapInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapPresenter extends BasePresenterImpl<MapViews> implements MapContract.MapPresenter,
        OnFinishedListener {

    //TODO don't working closeInfoWindow
    private Marker mMarker;

    private MapViews mapViews;
    private MapContract.MapInteractor mapInteractor;

    public MapPresenter(MapViews mapViews, MapContract.MapInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void attachView(MapViews mapViews) {
        super.attachView(mapViews);
        mapInteractor.findItems(this);
        mapInteractor.findItems1(this);
        mapInteractor.findMarker(this);
        mapInteractor.findMarker1(this);
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint, MapView mapView) {
        if (mMarker != null) {
            mMarker.closeInfoWindow();
        }
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        if (mMarker != null) {
            mMarker.closeInfoWindow();
            mMarker = null;
        } else {
            mMarker = marker;
        }

        return false;
    }

    @Override
    public void onFinished(List<GeoPoint> items) {
        mapViews.setPolyline(items);
    }

    @Override
    public void onFinished(GeoPoint geoPoint) {
        mapViews.setMarker(geoPoint);
    }

}

//For one marker: call marker.closeInfoWindow()
//For all markers: call InfoWindow.closeAllInfoWindowsOn(mapView)
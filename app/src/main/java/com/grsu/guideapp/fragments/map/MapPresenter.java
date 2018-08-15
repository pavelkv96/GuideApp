package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.map.MapContract.MapInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class MapPresenter extends BasePresenterImpl<MapViews> implements MapContract.MapPresenter,
        OnFinishedListener {

    private MapViews mapViews;
    private MapInteractor mapInteractor;

    public MapPresenter(MapViews mapViews, MapInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void getId(Integer id) {
        mapInteractor.getRouteById(this, id);
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        mapViews.animateMarker(marker, new GeoPoint(53.701254, 23.810049));
        return false;
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        try {
            Line line = null;
            for (Line encodePolyline : encodePolylines) {
                mapViews.setPolyline(decodeL(encodePolyline.getPolyline()));
                mapViews.setMarker(decodeP(encodePolyline.getStartPoint()));
                line = encodePolyline;
            }
            mapViews.setMarker(decodeP(line.getEndPoint()));
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }
}

//For one marker: call marker.closeInfoWindow()
//For all markers: call InfoWindow.closeAllInfoWindowsOn(mapView)
package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import com.grsu.guideapp.views.infowindows.CustomBasicInfoWindow;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

public enum PolylineSingleton {

    INSTANCE;

    public void getValue(@NonNull MapView mapView, @NonNull List<GeoPoint> geoPointList) {
        Polyline polyline = new Polyline(mapView);
        polyline.setPoints(geoPointList);
        //polyline.setInfoWindow(new CustomBasicInfoWindow(mapView));
        MarkerSingleton.setOverlaysView(mapView, polyline);
        mapView.invalidate();
        //return polyline;
    }
}

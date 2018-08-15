package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import com.grsu.guideapp.views.infowindows.CustomBasicInfoWindow;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

public enum PolylineSingleton {

    Polyline;

    public Polyline getPolyline(@NonNull MapView mapView, @NonNull List<GeoPoint> geoPointList) {
        Polyline polyline = new Polyline(mapView);
        polyline.setPoints(geoPointList);
        polyline.setInfoWindow(new CustomBasicInfoWindow(mapView));
        MarkerSingleton.setOverlaysView(mapView, polyline);
        return polyline;
    }

    public void removePolylines(@NonNull MapView mapView, @NonNull List<Polyline> polylines) {
        for (Polyline polyline : polylines) {
            removePolyline(mapView, polyline);
        }
    }

    public void removePolyline(@NonNull MapView mapView, @NonNull Polyline polyline) {
        mapView.getOverlays().remove(polyline);
    }
}

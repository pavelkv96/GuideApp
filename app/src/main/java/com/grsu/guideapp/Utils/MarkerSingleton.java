package com.grsu.guideapp.utils;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.views.infowindows.CustomMarkerInfoWindow;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

public enum MarkerSingleton {
    Marker;

    public Marker getMarker(@NonNull MapView mapView, @NonNull GeoPoint geoPoint) {
        return markerConstructor(mapView, geoPoint, null, false);
    }

    public Marker getMarkerWithBubble(@NonNull MapView mapView, @NonNull Poi poi) {
        return markerConstructor(mapView, null, poi, true);
    }

    private Marker markerConstructor(MapView mapView, GeoPoint geoPoint, Poi poi,
            boolean isVisibleBubble) {
        Marker marker = new Marker(mapView);
        if (geoPoint != null) {
            marker.setPosition(geoPoint);
        }
        if (poi != null) {
            marker.setPosition(new GeoPoint(poi.getLatitude(), poi.getLongitude()));
            marker = setIconByType(marker, poi.getType());
        }

        marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, isVisibleBubble));
        setOverlaysView(mapView, marker);

        return marker;
    }

    public static void setOverlaysView(@NonNull MapView mapView, @NonNull Overlay overlay) {
        mapView.getOverlays().add(overlay);
        mapView.invalidate();
    }

    public void removeMarkers(@NonNull MapView mapView, @NonNull List<Marker> markers) {
        for (Marker marker : markers) {
            marker.remove(mapView);
        }
    }

    private Marker setIconByType(Marker marker, Integer type) {
        Context context = ContextHolder.getContext();
        switch (type) {
            case 1:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.a_marker));
                break;
            case 2:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.b_marker));
                break;
            case 3:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.c_marker));
                break;
            case 4:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.d_marker));
                break;
        }

        return marker;
    }
}

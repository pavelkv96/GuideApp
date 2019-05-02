package com.grsu.guideapp.views.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.StorageUtils;

public class MyLocationLayer {

    private GoogleMap mMap;
    private static MyLocationLayer layer;
    private MarkerOptions options;
    private CircleOptions circleOptions;
    private Marker marker;
    private Circle circle;

    private static MyLocationLayer getInstance(Resources res) {
        if (layer == null) {
            layer = new MyLocationLayer(res);
        }
        return layer;
    }

    private MyLocationLayer(Resources res) {
        LatLng latLng = new LatLng(0, 0);
        Bitmap bitmap = StorageUtils.getBitmap(res, R.drawable.my_location);
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        if (circleOptions == null) {
            circleOptions = new CircleOptions();
        }
        circleOptions.visible(false);
        circleOptions.zIndex(.1f);
        circleOptions.strokeWidth(1);
        circleOptions.fillColor(res.getColor(R.color.myLocation));
        circleOptions.strokeColor(res.getColor(R.color.myLocation1));
        circleOptions.center(latLng);

        if (options == null) {
            options = new MarkerOptions();
        }
        options.visible(false);
        options.icon(descriptor);
        options.flat(true);
        options.anchor(.5f, .5f);
        options.position(latLng);
    }

    public void setMap(GoogleMap map) {
        clear();
        this.mMap = map;
        marker = mMap.addMarker(options);
        circle = mMap.addCircle(circleOptions);
    }

    public void setShow(boolean isShow) {
        marker.setVisible(isShow);
        circle.setVisible(isShow);
    }

    public void setLocation(Location location) {
        if (!marker.isVisible()) {
            marker.setVisible(true);
        }
        if (!circle.isVisible()) {
            circle.setVisible(true);
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        marker.setPosition(latLng);
        circle.setCenter(latLng);
        if (location.hasBearing()) {
            marker.setRotation(location.getBearing());
        }
        if (location.getAccuracy() != .0f) {
            circle.setRadius(location.getAccuracy());
        }
        setShow(true);
    }

    public LatLng getMyLocation() {
        return marker.getPosition();
    }

    public void clear() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        if (circle != null) {
            circle.remove();
            circle = null;
        }

        if (mMap != null) {
            mMap.clear();
            mMap = null;
        }
    }

    public static class Builder {

        private Resources res;

        public Builder() {
        }

        public Builder addResource(Resources res) {
            this.res = res;
            return this;
        }

        public MyLocationLayer build() {
            if (res == null) {
                throw new RuntimeException("The res cannot be null");
            }

            return MyLocationLayer.getInstance(res);
        }
    }
}

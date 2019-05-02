package com.grsu.guideapp.fragments.map;

import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.views.overlay.MyLocationLayer;

public class Animator implements Runnable {

    private static final int ANIMATE_SPEED = 500;
    private final Handler mHandler;
    private long startTime;
    private MyLocationLayer overlay;
    private LatLng beginLatLng;
    private LatLng endLatLng;
    private GoogleMap map;
    private float tilt = 54f;

    Animator(GoogleMap map, MyLocationLayer overlay) {
        this.map = map;
        this.overlay = overlay;
        mHandler = new Handler();
    }

    void startAnimation(LatLng start, LatLng end) {
        stopAnimation();
        this.startTime = SystemClock.uptimeMillis();
        beginLatLng = start;
        endLatLng = end;
        mHandler.postDelayed(this, 10);
    }

    private void stopAnimation() {
        mHandler.removeCallbacks(this);
    }

    public void setTilt(float tilt) {
        this.tilt = tilt;
    }

    @Override
    public void run() {
        double t = (float) (SystemClock.uptimeMillis() - startTime) / ANIMATE_SPEED;

        double lat = t * endLatLng.latitude + (1 - t) * beginLatLng.latitude;
        double lng = t * endLatLng.longitude + (1 - t) * beginLatLng.longitude;
        LatLng newPosition = new LatLng(lat, lng);

        Location location = MapUtils.toLocation(newPosition);
        location.setBearing(bearingBetweenLatLngs(beginLatLng, newPosition));
        overlay.setLocation(location);

        if (t < 1) {
            float bearing = bearingBetweenLatLngs(beginLatLng, newPosition);

            CameraPosition camera = createCamera(newPosition, bearing);
            map.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
            mHandler.postDelayed(this, 10);
        } else {
            stopAnimation();
        }
    }

    public CameraPosition createCamera(LatLng target, float bearing) {
        float zoom = map.getCameraPosition().zoom;
        Builder builder = new Builder();
        builder.target(target).bearing(bearing).tilt(tilt).zoom(zoom);
        return builder.build();
    }

    private static float bearingBetweenLatLngs(LatLng begin, LatLng end) {
        Location beginL = MapUtils.toLocation(begin);
        Location endL = MapUtils.toLocation(end);
        return beginL.bearingTo(endL);
    }
}

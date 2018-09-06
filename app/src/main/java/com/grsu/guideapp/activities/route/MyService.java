package com.grsu.guideapp.activities.route;

import static android.location.LocationManager.GPS_PROVIDER;
import static com.grsu.guideapp.project_settings.constants.Constants.KEY_GEO_POINT;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import com.grsu.guideapp.utils.MessageViewer.Logs;

public class MyService extends Service implements LocationListener {

    private static final String TAG = MyService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static final int INTERVAL = 1500;
    private static final float DISTANCE = -1f;

    @Override
    public void onCreate() {
        Logs.e(TAG, "onCreate");
        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(GPS_PROVIDER, INTERVAL, DISTANCE, this);
        } catch (SecurityException ex) {
            Logs.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Logs.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private void initializeLocationManager() {
        Logs.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logs.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        Logs.e(TAG, "onDestroy");
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(this);
            } catch (Exception ignore) {
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(RouteActivity.BR_ACTION);
        intent.putExtra(KEY_GEO_POINT, location);
        sendBroadcast(intent);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Logs.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Logs.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Logs.e(TAG, "onStatusChanged: " + provider);
    }
}
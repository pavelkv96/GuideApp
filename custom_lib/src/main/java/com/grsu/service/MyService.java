package com.grsu.service;

import android.Manifest.permission;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();

    private LocationManager manager;
    static int interval = 10000;
    static float distance = -1f;

    @RequiresPermission(permission.ACCESS_FINE_LOCATION)
    @Override
    public void onCreate() {
        super.onCreate();
        manager = ContextCompat.getSystemService(this, LocationManager.class);
        if (manager == null) {
            return;
        }
        try {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, distance, Class.getInstance);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, distance, Class.getInstance);
        } catch (SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
            stopSelf();
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
            stopSelf();
        }
        Log.e(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @RequiresPermission(permission.ACCESS_FINE_LOCATION)
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        if (manager != null) {
            manager.removeUpdates(Class.getInstance);
        }
        super.onDestroy();
    }
}

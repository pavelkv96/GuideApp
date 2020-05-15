package com.grsu.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    public static final String PACKAGE_NAME = "com.grsu.location";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + "broadcast";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + "location";

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null) onNewLocation(locationResult.getLastLocation());
        }

        @Override
        public void onLocationAvailability(@Nullable LocationAvailability availability) {
            super.onLocationAvailability(availability);
            if (availability != null) onNewAvailability(availability.isLocationAvailable());
            else onNewAvailability(false);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10_000L);
        mLocationRequest.setFastestInterval(2_000L);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Routes of the Awgustow Canal";
            NotificationChannel mChannel = new NotificationChannel("channel_01", name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager mNotificationManager = ContextCompat.getSystemService(this, NotificationManager.class);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates." + unlikely, unlikely);
        }

    }

    private void onNewAvailability(boolean isLocationAvailable) {
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, isLocationAvailable);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onNewLocation(Location location) {
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates." + unlikely, unlikely);
        }

        super.onDestroy();
    }
}

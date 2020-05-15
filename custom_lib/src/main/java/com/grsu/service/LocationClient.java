package com.grsu.service;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public final class LocationClient implements OnReceiverResponse {

    public static final LocationClient INSTANCE;
    private static final String TAG = LocationClient.class.getSimpleName();

    private LocationClient() {
    }

    static {
        INSTANCE = new LocationClient();
    }

    @Nullable
    private LocationListener listener;

    @Nullable
    private MyReceiver receiver = null;

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void connect(@NonNull Context applicationContext, LocationListener listener) {
        if (ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_FINE_LOCATION) == 0) {
            applicationContext.startService(new Intent(applicationContext, MyService.class));
            receiver = new MyReceiver(this);
            IntentFilter filter = new IntentFilter(MyService.ACTION_BROADCAST);
            LocalBroadcastManager.getInstance(applicationContext).registerReceiver(receiver, filter);
            this.listener = listener;
        }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void disconnect(@NonNull Context applicationContext) {
        applicationContext.stopService(new Intent(applicationContext, MyService.class));
        listener = null;
        if (receiver != null) {
            LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(receiver);
        }
        receiver = null;
    }

    @Override
    public void onReceiver(@NonNull Intent intent) {
        if (intent.getExtras() != null) {
            Object o = intent.getExtras().get(MyService.EXTRA_LOCATION);
            Log.e(TAG, "Get object");
            if (o instanceof Boolean) {
                boolean a = (boolean) o;
                Log.e(TAG, "Get availability " + a);
                if (listener == null) return;
                if (a) listener.onProviderEnabled("gps");
                else listener.onProviderDisabled("gps");
            } else if (o instanceof Location) {
                Location location = (Location) o;
                if (listener == null) return;
                listener.onLocationChanged(location);
                Log.e(TAG, "Get location " + location.getLatitude() + " : " + location.getLongitude());
            }
        }
    }
}

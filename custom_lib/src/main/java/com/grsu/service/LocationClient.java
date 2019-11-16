package com.grsu.service;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import android.util.Log;

public class LocationClient implements LocationListener {

    private static final String TAG = LocationClient.class.getSimpleName();
    private final Context context;
    private final Listener listener;

    private LocationClient(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void connect() {
        //ContextCompat.startForegroundService(context, new Intent(context, MyService.class));
        if (ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION) == 0) {
            //Location location = getLastLocation();
            //if (location == null) {
            context.startService(new Intent(context, MyService.class));
            Class.getInstance.registerCallback(this);
            /*} else {
                listener.onChangedLocation(location);
            }*/
        }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void disconnect() {
        Class.getInstance.unregisterCallback(this);
        context.stopService(new Intent(context, MyService.class));
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public Location getLastLocation() {
        LocationManager manager = ContextCompat.getSystemService(context, LocationManager.class);
        if (manager == null) {
            return null;
        }

        Location bestLocation = null;
        for (String provider : manager.getProviders(true)) {
            Location l = manager.getLastKnownLocation(provider);
            Log.e(TAG, "getLastLocation: " + provider + "   " + l);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        listener.onChangedLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
        try {
            ((OnLocationListener) listener).onStatusChanged(provider, status, bundle);
        } catch (ClassCastException ignore) {
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        try {
            ((OnLocationListener) listener).onProviderDisabled(provider);
        } catch (ClassCastException ignore) {
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        try {
            ((OnLocationListener) listener).onProviderEnabled(provider);
        } catch (ClassCastException ignore) {
        }
    }

    public static final class Builder {

        private Context context;
        private int interval;
        private int distance;
        private Listener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder setDistance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder addListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public LocationClient build() {
            if (distance > 0) {
                MyService.distance = distance;
            }
            if (interval > 0) {
                MyService.interval = interval;
            }

            if (listener == null) {
                throw new RuntimeException("The listener cannot be null");
            }

            return new LocationClient(context, listener);
        }
    }
}

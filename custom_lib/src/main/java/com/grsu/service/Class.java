package com.grsu.service;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import java.util.HashSet;
import java.util.Set;

enum Class implements LocationListener {

    getInstance;

    private Set<LocationListener> listeners;

    Class() {
        listeners = new HashSet<>();
    }

    void registerCallback(LocationListener callback) {
        listeners.add(callback);
    }

    void unregisterCallback(LocationListener callback) {
        listeners.remove(callback);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (listeners != null && !listeners.isEmpty()) {
            for (LocationListener listener : listeners) {
                listener.onLocationChanged(location);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (listeners != null && !listeners.isEmpty()) {
            for (LocationListener listener : listeners) {
                listener.onProviderDisabled(provider);
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (listeners != null && !listeners.isEmpty()) {
            for (LocationListener listener : listeners) {
                listener.onProviderEnabled(provider);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (listeners != null && !listeners.isEmpty()) {
            for (LocationListener listener : listeners) {
                listener.onStatusChanged(provider, status, extras);
            }
        }
    }
}

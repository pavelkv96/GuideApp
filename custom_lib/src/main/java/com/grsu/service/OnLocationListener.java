package com.grsu.service;

import android.os.Bundle;

public interface OnLocationListener extends Listener {

    void onProviderEnabled(String provider);

    void onProviderDisabled(String provider);

    void onStatusChanged(String provider, int status, Bundle bundle);
}

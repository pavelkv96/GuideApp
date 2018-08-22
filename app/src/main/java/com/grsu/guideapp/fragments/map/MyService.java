package com.grsu.guideapp.fragments.map;

import static android.location.LocationManager.GPS_PROVIDER;
import static com.grsu.guideapp.utils.Constants.KEY_GEO_POINT_1;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class MyService extends Service implements LocationListener {

    private static final String TAG = MyService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = -1f;
    private Location mLastLocation;
    private ArrayList<GeoPoint> geoPoints;
    private int i = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
        } catch (SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(this);
            } catch (Exception ex) {
                Log.e(TAG, "fail to remove location listners, ignore", ex);
            }
        }
    }

    private void initializeLocationManager() {
        mLastLocation = new Location(GPS_PROVIDER);
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);
            geoPoints = new ArrayList<>();
            geoPoints.addAll(getList());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        /*if (i + 1 == 21) {
            onDestroy();
        } else {
            GeoPoint geoPoint = geoPoints.get(i);
            i++;*/
        /*Location currentLocation = new Location("");
        currentLocation.setLatitude(geoPoint.getLatitude());
        currentLocation.setLongitude(geoPoint.getLongitude());
        mLastLocation.set(currentLocation);*/

        Intent intent = new Intent(MapFragment.BR_ACTION);
        intent.putExtra(KEY_GEO_POINT_1, (Parcelable) location);
        sendBroadcast(intent);
        /*}*/
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }

    //TODO TEST DATA
    private List<GeoPoint> getList() {
        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(new GeoPoint(53.697250000000004, 23.83509));
        geoPointList.add(new GeoPoint(53.698060, 23.835082));
        geoPointList.add(new GeoPoint(53.698240000000006, 23.835610000000003));//2
        geoPointList.add(new GeoPoint(53.69821, 23.83564));//3
        geoPointList.add(new GeoPoint(53.69778, 23.836060000000003));//4
        geoPointList.add(new GeoPoint(53.697540000000004, 23.836240000000004));//5
        geoPointList.add(new GeoPoint(53.697340000000004, 23.836350000000003));//6
        geoPointList.add(new GeoPoint(53.69715000000001, 23.836430000000004));//7
        geoPointList.add(new GeoPoint(53.696650000000005, 23.83658));//8
        geoPointList.add(new GeoPoint(53.696310000000004, 23.83667));//9
        geoPointList.add(new GeoPoint(53.69596000000001, 23.83676));//10
        geoPointList.add(new GeoPoint(53.695690000000006, 23.836910000000003));//11
        geoPointList.add(new GeoPoint(53.695600000000006, 23.83697));//12
        geoPointList.add(new GeoPoint(53.695570000000004, 23.836990000000004));//13
        geoPointList.add(new GeoPoint(53.69548, 23.837090000000003));//14
        geoPointList.add(new GeoPoint(53.69541, 23.83716));//15
        geoPointList.add(new GeoPoint(53.695330000000006, 23.83727));//16
        geoPointList.add(new GeoPoint(53.69525, 23.83743));//17
        geoPointList.add(new GeoPoint(53.695150000000005, 23.83762));//18
        geoPointList.add(new GeoPoint(53.695080000000004, 23.83773));//19

        return geoPointList;
    }

    private void getSleep() throws InterruptedException {
        switch (i) {
            case 3:
                Thread.sleep(3000);
                break;
        }
    }
}

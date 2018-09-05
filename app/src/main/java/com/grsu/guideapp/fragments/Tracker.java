package com.grsu.guideapp.fragments;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.GPS_PROVIDER;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.osmdroid.tileprovider.util.StorageUtils;

public class Tracker extends Fragment implements LocationListener {

    TextView data;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 10;
    private Location mLastLocation;
    private File file = null;
    FileOutputStream stream = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tracker, container, false);

        data = view.findViewById(R.id.tv_fragment_tracker_data);

        view.findViewById(R.id.btn_fragment_tracker_start)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initializeLocationManager();

                        file = new File(StorageUtils.getStorage() + "/tracker.txt");
                        Logs.e("TAG", file.getAbsolutePath());
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            file.delete();
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            stream = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        try {
                            mLocationManager.requestLocationUpdates(
                                    GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                    Tracker.this);
                        } catch (SecurityException ignored) {
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                });

        view.findViewById(R.id.btn_fragment_tracker_stop)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (mLocationManager != null) {
                            try {
                                mLocationManager.removeUpdates(Tracker.this);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                });

        return view;
    }

    private void initializeLocationManager() {
        mLastLocation = new Location(GPS_PROVIDER);
//        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity()
                    .getSystemService(LOCATION_SERVICE);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Logs.e("TAG", location.toString());
        LatLng latLng = MapUtils.toLatLng(location);
        data.setText(latLng.toString());
        try {
            stream.write(String.valueOf(
                    "latLngList.add(new LatLng(" + latLng + "));\n")
                    .getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @NonNull
    public static Tracker newInstance() {
        return new Tracker();
    }
}

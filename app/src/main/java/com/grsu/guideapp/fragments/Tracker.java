package com.grsu.guideapp.fragments;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tracker extends Fragment implements LocationListener {

//    @BindView(R.id.tv_fragment_tracker_data)
    TextView data;
    private LocationManager mLocationManager = null;
    private static final int INTERVAL = 1000;
    private static final float DISTANCE = 0;
    FileOutputStream stream = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
//        ButterKnife.bind(this, view);
        return view;
    }

//    @OnClick(R.id.btn_fragment_tracker_start)
    public void start() {
        initializeLocationManager();

        File file = new File(StorageUtils.getStorage() + "/tracker.txt");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            stream = new FileOutputStream(file);
            mLocationManager.requestLocationUpdates(GPS_PROVIDER, INTERVAL, DISTANCE, Tracker.this);
            mLocationManager.requestLocationUpdates(NETWORK_PROVIDER, INTERVAL, DISTANCE, Tracker.this);
        } catch (SecurityException | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

//    @OnClick(R.id.btn_fragment_tracker_stop)
    public void stop() {

        StreamUtils.closeStream(stream);
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(Tracker.this);
            } catch (Exception ignored) {
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null && getActivity() != null) {
            mLocationManager = ContextCompat.getSystemService(getActivity(), LocationManager.class);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Logs.e("TAG", location.toString());
        LatLng latLng = MapUtils.toLatLng(location);
        String text = location.getProvider() + "  " + location.getAccuracy() + "   " + latLng.toString() + "  " + location.getBearing();
        data.setText(text);
        try {
            stream.write(("latLngList.add(new LatLng(" + latLng.latitude + ", " + latLng.longitude
                    + "));   " + location.getBearing() + "\n").getBytes());
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

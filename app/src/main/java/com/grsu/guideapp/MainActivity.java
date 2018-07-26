package com.grsu.guideapp;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 1;
    MapView map = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());*/
        Log.e(TAG, "onCreate: " + StorageUtils.getStorage());

        setContentView(R.layout.activity_main);

        int permissionStatus = ContextCompat
                .checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            permission.WRITE_EXTERNAL_STORAGE,
                            permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_READ_CONTACTS);
        }

        map = findViewById(R.id.map);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(11.0);
        map.setExpectedCenter(new GeoPoint(48.8583, 2.2944));
        map.getController().setZoom(11.0);
        map.setBuiltInZoomControls(false);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setUseDataConnection(false);

        /*if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

            Marker startMarker = new Marker(map);
            startMarker.setPosition(new GeoPoint(48.8583, 2.2944));
            startMarker.setTitle("drgdgd");
            map.getOverlays().add(startMarker);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Context ctx = getApplicationContext();
                    Configuration.getInstance()
                            .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
                    Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());
                    Log.e(TAG, "onCreate: " + StorageUtils.getStorage());
                    Log.e(TAG, "onCreate: " + StorageUtils.getSdCardPath());

                } else {
                    Log.e(TAG, "onRequestPermissionsResult: ");
                    finish();
                }
        }
    }

}
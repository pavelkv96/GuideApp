package com.grsu.guideapp.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.mf.MapsForgeTileSource;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;

public abstract class BaseMapFragment<P extends BasePresenter, A extends FragmentActivity>
        extends BaseFragment<P, A>
        implements OnMapReadyCallback, TileProvider, OnMarkerClickListener {

    private MapView mapView;
    protected GoogleMap mMap;

    protected abstract @IdRes
    int getFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (CheckSelfPermission.writeExternalStorageIsGranted(getActivity)) {
            mapView = rootView.findViewById(getFragment());
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        File file = getActivity.getDatabasePath(Settings.MAP_FILE);
        Logs.e(getTags(), "Loaded map file " + file.exists());
        if (CheckSelfPermission.writeExternalStorageIsGranted(getActivity) && file.exists()) {
            mapView.onStart();
            MapsForgeTileSource.createFromFiles(file, getActivity);
        } else {
            popBackStack();
        }
    }

    @Override
    public void onStop() {
        if (mapView != null) {
            mapView.onStop();
        }
        MapsForgeTileSource.dispose();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setMinZoomPreference(Settings.MIN_ZOOM_LEVEL);
        mMap.setMaxZoomPreference(Settings.MAX_ZOOM_LEVEL);

        LatLngBounds borders = new LatLngBounds(Settings.NORTH_WEST, Settings.SOUTH_EAST);
        mMap.setLatLngBoundsForCameraTarget(borders);

        mMap.setOnMarkerClickListener(this);

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(this));
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        long tileIndex = MapUtils.getTileIndex(zoom, x, y);
        byte[] tile = MapsForgeTileSource.loadTile(tileIndex);

        return tile != null ? new Tile(256, 256, tile) : NO_TILE;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
}

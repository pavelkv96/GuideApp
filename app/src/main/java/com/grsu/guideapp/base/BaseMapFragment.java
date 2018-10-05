package com.grsu.guideapp.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.databases.CacheDBHelper;
import com.grsu.guideapp.mf.MapsForgeTileSource;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.MapUtils;
import java.io.File;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public abstract class BaseMapFragment<P extends BasePresenter> extends BaseFragment<P> implements
        OnMapReadyCallback, TileProvider, OnMarkerClickListener {

    protected GoogleMap mMap;

    protected abstract @IdRes
    int getFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(getFragment()));
        mapFragment.getMapAsync(this);

        return rootView;
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
    public void onStart() {
        super.onStart();
        if (CheckSelfPermission.writeExternalStorageIsGranted(getContext())) {
            getActivity().finish();
        }

        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        new CacheDBHelper(getContext());
        File file = getActivity().getDatabasePath(Settings.MAP_FILE);
        Toast.makeText(getContext(), "Loaded map file " + file.exists(), Toast.LENGTH_LONG).show();
        if (file.exists()) {
            MapsForgeTileSource.createFromFiles(new File[]{file}, null, Settings.CURRENT_PROVIDER);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        CacheDBHelper.refreshDb();
        MapsForgeTileSource.dispose();
        AndroidGraphicFactory.clearResourceMemoryCache();
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        long tileIndex = MapUtils.getTileIndex(zoom, x, y);
        byte[] tile = MapsForgeTileSource.loadTile(tileIndex);

        return tile != null ? new Tile(256, 256, tile) : NO_TILE;
    }
}

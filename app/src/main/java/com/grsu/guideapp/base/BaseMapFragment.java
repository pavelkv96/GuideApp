package com.grsu.guideapp.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.databases.CacheDBHelper;
import com.grsu.guideapp.utils.MapUtils;

public abstract class BaseMapFragment<P extends BasePresenter> extends BaseFragment<P> implements
        OnMapReadyCallback, TileProvider {

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
        mMap.setMinZoomPreference(13);
        mMap.setMaxZoomPreference(19);

        LatLngBounds borders = new LatLngBounds(new LatLng(53.5986, 23.7099),
                new LatLng(53.7597, 23.9845));
        mMap.setLatLngBoundsForCameraTarget(borders);

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(this));
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        long tileIndex = MapUtils.getTileIndex(zoom, x, y);
        byte[] bytes = CacheDBHelper.getTile(MapUtils.getIndex(tileIndex), "Mapsforge");
        if (bytes != null) {
            return new Tile(256, 256, bytes);
        }

        return NO_TILE;
    }
}

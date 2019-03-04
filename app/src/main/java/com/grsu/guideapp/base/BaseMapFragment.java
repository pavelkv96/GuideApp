package com.grsu.guideapp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.mf.MapsForgeTileSource;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import java.io.File;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

public abstract class BaseMapFragment<P extends BasePresenter, A extends FragmentActivity>
        extends BaseFragment<P, A>
        implements OnMapReadyCallback, TileProvider, OnMarkerClickListener {

    protected GoogleMap mMap;
    protected TileOverlay overlay;

    protected abstract @IdRes
    int getMap();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Fragment mapFragment = getChildFragmentManager().findFragmentById(getMap());
        if (mapFragment != null) {
            ((SupportMapFragment) mapFragment).getMapAsync(this);
        }

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setMinZoomPreference(Settings.MIN_ZOOM_LEVEL);
        mMap.setMaxZoomPreference(Settings.MAX_ZOOM_LEVEL);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        File file = getActivity.getDatabasePath(Settings.MAP_FILE);
        LatLngBounds borders = MapsForgeTileSource.getBoundingBox(file);
        mMap.setLatLngBoundsForCameraTarget(borders);

        mMap.setOnMarkerClickListener(this);

        overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CheckSelfPermission.writeExternalStorageIsGranted(getContext())) {
            getActivity.finish();
        }

        AndroidGraphicFactory.createInstance(getActivity.getApplication());
        new CacheDBHelper(getContext());

        File file = getActivity.getDatabasePath(Settings.MAP_FILE);

        Toasts.makeL(getContext(), "Loaded map file " + file.exists());

        if (file.exists()) {
            XmlRenderTheme theme = null;
            try {
                Context context = getActivity.getApplicationContext();
                theme = new AssetsRenderTheme(context, Settings.THEME_FOLDR, Settings.THEME_FILE);
            } catch (Exception ignore) {
            }
            MapsForgeTileSource.createFromFiles(file, theme, Settings.CURRENT_PROVIDER);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        overlay.clearTileCache();
        CacheDBHelper.refreshDb();
        MapsForgeTileSource.dispose();
        AndroidGraphicFactory.clearResourceMemoryCache();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        long tileIndex = MapUtils.getTileIndex(zoom, x, y);
        byte[] tile = MapsForgeTileSource.loadTile(tileIndex);

        return tile != null ? new Tile(256, 256, tile) : NO_TILE;
    }

    @Override
    public void onDestroyView() {
        if (overlay != null) {
            overlay.remove();
        }
        super.onDestroyView();
    }
}

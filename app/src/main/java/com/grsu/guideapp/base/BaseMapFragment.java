package com.grsu.guideapp.base;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.adapters.TileAdapter;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.Provider;
import com.grsu.guideapp.views.overlay.MyLocationLayer;
import com.grsu.guideapp.views.overlay.MyLocationLayer.Builder;
import java.io.File;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import timber.log.Timber;

public abstract class BaseMapFragment<P extends BasePresenter, A extends FragmentActivity>
        extends BaseFragment<P, A>
        implements OnMapReadyCallback, TileProvider, OnMarkerClickListener, OnCameraMoveListener,
        OnCameraIdleListener, Runnable {

    protected GoogleMap mMap;
    protected TileOverlay overlay;
    protected MyLocationLayer myLocation;
    private LatLngBounds borders;
    private float previousZoom = -1.0f;
    private Handler handler;

//    @BindView(R.id.map_zoom_control)
    protected RelativeLayout map_zoom_control;

//    @OnClick(R.id.map_zoom_in)
    protected void zoomIn() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

//    @OnClick(R.id.map_zoom_out)
    protected void zoomOut() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

//    @BindView(R.id.scaleView)
    View scaleView;

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
        handler = new Handler();

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setMinZoomPreference(Settings.MIN_ZOOM_LEVEL);
        mMap.setMaxZoomPreference(Settings.MAX_ZOOM_LEVEL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);

        File file = getActivity.getDatabasePath(Settings.MAP_FILE);
        borders = TileAdapter.INSTANCE.getBoundingBox(file);
        mMap.setLatLngBoundsForCameraTarget(borders);

        mMap.setOnMarkerClickListener(this);

        overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(this));
        myLocation = new Builder().addResource(getResources()).build();
        myLocation.setMap(mMap);
    }

    @Override
    public void onStart() {
        super.onStart();
        File file = getActivity.getDatabasePath(Settings.MAP_FILE);
        if (!CheckPermission.checkStoragePermission(getContext()) || !file.exists()) {
            getActivity.finish();
            return;
        }

        AndroidGraphicFactory.createInstance(getActivity.getApplication());
        TileAdapter.INSTANCE.createConnection(file, Provider.FORGE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (overlay != null) {
            overlay.clearTileCache();
        }
        TileAdapter.INSTANCE.dispose();
        AndroidGraphicFactory.clearResourceMemoryCache();
        handler.removeCallbacks(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        return TileAdapter.INSTANCE.loadTile(x, y, zoom);
    }

    @Override
    public void onDestroyView() {
        if (overlay != null) {
            overlay.remove();
        }
        myLocation.clear();
        super.onDestroyView();
    }

    @Override
    public void onCameraMove() {
        /*Log.e("TAG", "onCameraMove: ");
        if (scaleView != null && scaleView.getVisibility() != View.GONE) {
            updateScaleView();
        }*/
    }

    @Override
    public void onCameraIdle() {
        Timber.e("onCameraIdle: ");
        if (scaleView != null && scaleView.getVisibility() != View.GONE) {
            updateScaleView();
        }
    }

    private void updateScaleView() {
        CameraPosition position = mMap.getCameraPosition();
        if (previousZoom != position.zoom) {
            handler.removeCallbacks(this);
            scaleView.setVisibility(View.VISIBLE);
            handler.postDelayed(this, 3500);
//            scaleView.update(position.zoom, position.target.latitude);
            previousZoom = position.zoom;
        }
    }

    protected LatLngBounds getBorders() {
        return borders;
    }

    @Override
    public void run() {
        scaleView.setVisibility(View.INVISIBLE);
    }
}

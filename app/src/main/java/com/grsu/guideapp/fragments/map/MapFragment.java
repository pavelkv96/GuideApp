package com.grsu.guideapp.fragments.map;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.PolylineSingleton;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class MapFragment extends BaseFragment<MapPresenter> implements MapEventsReceiver {

    Marker singletonValue = null;

    @BindView(R.id.map)
    MapView map;

    @Override
    public void onAttach(Context context) {
        if (context != null) {
            int permissionStatus = ContextCompat
                    .checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        }
        super.onAttach(context);
    }

    @NonNull
    @Override
    protected MapPresenter getPresenterInstance() {
        return new MapPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(11.0);
        map.setExpectedCenter(new GeoPoint(48.8583, 2.2944));
        map.getController().setZoom(11.0);
        map.setBuiltInZoomControls(false);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setUseDataConnection(false);

        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(new GeoPoint(48.8583, 2.2944));
        geoPointList.add(new GeoPoint(47.8583, 2.2944));
        geoPointList.add(new GeoPoint(46.8583, 2.2944));
        geoPointList.add(new GeoPoint(45.8583, 2.2944));

        List<GeoPoint> geoPointList1 = new ArrayList<>();
        geoPointList1.add(new GeoPoint(48.8583, 2.2944));
        geoPointList1.add(new GeoPoint(48.8583, 1.2944));
        geoPointList1.add(new GeoPoint(48.8583, 0.2944));
        geoPointList1.add(new GeoPoint(48.8583, -1.2944));

        PolylineSingleton polylineSingleton = PolylineSingleton.INSTANCE;

        polylineSingleton.getValue(map, geoPointList);
        polylineSingleton.getValue(map, geoPointList1);

        MarkerSingleton markerSingleton = MarkerSingleton.INSTANCE;

        singletonValue = markerSingleton.getValue(map, new GeoPoint(48.8583, 2.2944));
        markerSingleton.getValue(map, new GeoPoint(47.8583, 2.2944));
        map.getOverlays().add(new MapEventsOverlay(this));

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
    public boolean singleTapConfirmedHelper(GeoPoint p) {

        InfoWindow.closeAllInfoWindowsOn(map);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}

//For one marker: call marker.closeInfoWindow()
//For all markers: call InfoWindow.closeAllInfoWindowsOn(map)
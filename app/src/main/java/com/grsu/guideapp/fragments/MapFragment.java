package com.grsu.guideapp.fragments;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.grsu.guideapp.R;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {

    private static final String TAG = "MapFragment";

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Context ctx = inflater.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        ButterKnife.bind(this, view);

        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(11.0);
        map.setExpectedCenter(new GeoPoint(48.8583, 2.2944));
        map.getController().setZoom(11.0);
        map.setBuiltInZoomControls(false);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setUseDataConnection(false);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(48.8583, 2.2944));
        startMarker.setTitle("drgdgd");
        map.getOverlays().add(startMarker);

        return view;
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

}

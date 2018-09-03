package com.grsu.guideapp.fragments.test;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.database.DBHelper;
import com.grsu.guideapp.R;

public class TestAnimationFragment extends Fragment implements OnMapReadyCallback, TileProvider {

    private GoogleMap mMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_animation, container, false);

        new DBHelper(getContext());
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(MAP_TYPE_NONE);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(53.899045, 23.624377);
        mMap.addMarker(
                new MarkerOptions().position(sydney).title("Marker in Sydney").snippet("ssesgges"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(this));
    }


    @Override
    public Tile getTile(int x, int y, int zoom) {

        byte[] bytes = DBHelper.getTile(x, y, zoom, "Mapsforge");
        if (bytes == null) {
            return NO_TILE;
        }

        return new Tile(256, 256, bytes);
    }

    @Override
    public void onDestroyView() {
        DBHelper.refreshDb();
        super.onDestroyView();
    }
}

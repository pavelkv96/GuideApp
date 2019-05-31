package com.grsu.guideapp.fragments.map_preview_v1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import java.util.ArrayList;
import java.util.List;

public abstract class MapPreviewFragment<P extends MapPreviewPresenter> extends
        BaseMapFragment<P, RouteActivity> implements MapPreviewViews, OnMapClickListener {

    private static final String TAG = MapPreviewFragment.class.getSimpleName();
    private List<Marker> nearPoi = new ArrayList<>();

    protected int route = -1;
    protected LatLngBounds bounds;
    protected Polyline polyline;

    @NonNull
    @Override
    protected P getPresenterInstance() {
        return (P) new MapPreviewPresenter(this, new MapPreviewInteractor(null));
    }

    @Override
    protected int getMap() {
        return R.id.map;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (route != -1) {
            mPresenter.getId(route);
        } else {
            if (getActivity != null) {
                Toasts.makeL(getActivity, getString(R.string.error_please_refresh_your_database));
                getActivity.getSupportFragmentManager().popBackStack();
                getActivity.finish();
            }
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    //-------------------------------------------
    //	Summary: implements Contracts
    //-------------------------------------------

    @Override
    public void setPolyline(final List<LatLng> geoPointList) {
        PolylineOptions options = new PolylineOptions();
        options.color(Color.BLUE);
        options.width(20);
        options.zIndex(1);
        options.addAll(geoPointList);
        polyline = mMap.addPolyline(options);
    }

    @Override
    public void setPointsTurn(LatLng latLng, int icon) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.fromResource(icon));
        mMap.addMarker(options);
    }

    @Override
    public void setPoi(Poi poi) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(poi.getLocation());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(poi.getIcon()));
        Marker marker = mMap.addMarker(markerOptions);
        nearPoi.add(marker);
    }

    @Override
    public void removePoi() {
        for (Marker marker : nearPoi) {
            marker.remove();
        }
        nearPoi.clear();
    }

    @Override
    public void initData() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        Integer choiceItem = read(Constants.KEY_SINGLE_CHOICE_ITEM, Integer.class);
        mPresenter.setRadius(choiceItem);
        mPresenter.getAllPoi(true);
    }

    protected void setUI(boolean flag) {
        if (flag) {
            mMap.setOnMapClickListener(this);
        } else {
            mMap.setOnMapClickListener(null);
        }
        mMap.getUiSettings().setScrollGesturesEnabled(flag);
        mMap.getUiSettings().setAllGesturesEnabled(flag);
        mMap.getUiSettings().setTiltGesturesEnabled(flag);
    }

    @Override
    public void onDestroyView() {
        Logs.e(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();
        polyline.setWidth(mMap.getCameraPosition().zoom + 5);
    }
}

package com.grsu.guideapp.fragments.open_route;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.open_route.RoutePreviewContract.RoutePresenter;
import com.grsu.guideapp.fragments.open_route.RoutePreviewContract.RouteViews;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;
import java.util.List;

public class OpenRouteFragment extends BaseMapFragment<RoutePresenter, RouteActivity>
        implements RouteViews {

    @NonNull
    @Override
    protected RoutePresenter getPresenterInstance() {
        return new RoutePreviewPresenter(new RoutePreviewInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.map;
    }

    @Override
    protected String getTitle() {
        return "Error name";
    }

    @Override
    protected int getMap() {
        return R.id.map;
    }

    private int id = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(Constants.KEY_ID_ROUTE);
            mPresenter.getRouteById(id);
        } else {
            getActivity.getSupportFragmentManager().popBackStack();
        }

        return rootView;
    }

    public static OpenRouteFragment newInstance(Integer id) {
        OpenRouteFragment fragment = new OpenRouteFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_ID_ROUTE, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        if (id != -1) {
            mPresenter.getRouteById(id);
            mPresenter.getLines(id);
            mPresenter.getPoi(id);
        }
    }

    @Override
    public void setBounds(DtoRoute route) {
        LatLng southwest = CryptoUtils.decodeP(route.getSouthwest());
        LatLng northeast = CryptoUtils.decodeP(route.getNortheast());
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    @Override
    public void setLines(List<Line> lines) {
        if (!lines.isEmpty()) {
            LatLng start = CryptoUtils.decodeP(lines.get(0).getStartPoint());
            LatLng end = CryptoUtils.decodeP(lines.get(lines.size() - 1).getEndPoint());

            MarkerOptions startMarker = new MarkerOptions();
            startMarker.position(start);
            startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.a_marker));

            MarkerOptions endMarker = new MarkerOptions();
            endMarker.position(end);
            endMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.b_marker));

            mMap.addMarker(startMarker);
            mMap.addMarker(endMarker);

            for (Line line : lines) {
                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(Color.BLUE)
                        .width(15)
                        .zIndex(1)
                        .startCap(new SquareCap()).endCap(new SquareCap())
                        .addAll(CryptoUtils.decodeL(line.getPolyline()));
                mMap.addPolyline(polylineOptions);
            }
        }
    }

    @Override
    public void setPoi(List<Poi> poiList) {
        for (Poi poi: poiList) {
            MarkerOptions poiMarker = new MarkerOptions();
            poiMarker.position(poi.getLocation());
            poiMarker.icon(BitmapDescriptorFactory.fromBitmap(poi.getIcon()));
            mMap.addMarker(poiMarker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}

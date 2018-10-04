package com.grsu.guideapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.base.BasePresenter;

public class MapFragment extends BaseMapFragment implements OnMapReadyCallback, TileProvider {

    @Override
    protected int getFragment() {
        return R.id.fragment_map_map;
    }

    @NonNull
    @Override
    protected BasePresenter getPresenterInstance() {
        return null;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    public static MapFragment newInstance() {

        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

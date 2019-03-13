package com.grsu.guideapp.fragments.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewFragment;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps;
import com.grsu.ui.bottomsheet.BottomSheetCallback;

public class TestFragment extends MapPreviewFragment<TestPresenter> implements BottomSheetCallback,
        TestContract.TestViews {

    private static final String TAG = TestFragment.class.getSimpleName();
    private BottomSheetBehaviorGoogleMaps behavior;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout fragment;

    @NonNull
    @Override
    protected TestPresenter getPresenterInstance() {
        return new TestPresenter(this, new TestInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    protected String getTitle() {
        return "Test";
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_test;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        String name = getTitle();
        Bundle bundle = getArguments();
        if (bundle != null) {
            route = bundle.getInt(Constants.KEY_ID_ROUTE, -1);
            //name = bundle.getString(Constants.KEY_NAME_ROUTE);
            LatLng southwest = CryptoUtils.decodeP(bundle.getString(Constants.KEY_SOUTHWEST));
            LatLng northeast = CryptoUtils.decodeP(bundle.getString(Constants.KEY_NORTHEAST));
            bounds = new LatLngBounds(southwest, northeast);
        }

        super.onCreateView(inflater, container, savedInstanceState);

        getActivity.setTitleToolbar(name);

        coordinatorLayout = rootView.findViewById(R.id.coordinator_layout);
        fragment = rootView.findViewById(R.id.map_include);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehaviorGoogleMaps.from(bottomSheet);
        behavior.setHideable(false);
        behavior.addBottomSheetCallback(this);
        behavior.setState(BottomSheetBehaviorGoogleMaps.STATE_ANCHOR_POINT);
        return rootView;
    }

    private void updateViewSize(float offset) {
        LayoutParams params = fragment.getLayoutParams();
        int max = coordinatorLayout.getHeight();
        params.height = (int) (max - max * offset - behavior.getPeekHeight() * (1 - offset));
        fragment.setLayoutParams(params);
    }

//    private int convertToPixels(float value) {
//        return (int) (coordinatorLayout.getHeight() * value - behavior.getPeekHeight() * (value));
//    }

    public static TestFragment newInstance(Bundle args) {
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //behavior.setAnchorPoint(convertToPixels(.5f));
        //updateViewSize(.5f);
        super.onMapReady(googleMap);
        setUI(false);
    }

    void setUI(boolean flag) {
        mMap.getUiSettings().setScrollGesturesEnabled(flag);
        mMap.getUiSettings().setAllGesturesEnabled(flag);
        mMap.getUiSettings().setTiltGesturesEnabled(flag);
        if (flag) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        switch (newState) {
            case BottomSheetBehaviorGoogleMaps.STATE_DRAGGING: {
                Log.e(TAG, "onStateChanged: dragging");
            }
            break;
            case BottomSheetBehaviorGoogleMaps.STATE_ANCHOR_POINT: {
                Log.e(TAG, "onStateChanged: anchor_point");
                setUI(false);
            }
            break;
            case BottomSheetBehaviorGoogleMaps.STATE_EXPANDED: {
                Log.e(TAG, "onStateChanged: expanded");
            }
            break;
            case BottomSheetBehaviorGoogleMaps.STATE_COLLAPSED: {
                Log.e(TAG, "onStateChanged: collapsed");
                setUI(true);
            }
            break;
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (slideOffset <= .36f) {
            Log.e(TAG, "onSlide: " + slideOffset);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
            updateViewSize(slideOffset);
        }
    }
}

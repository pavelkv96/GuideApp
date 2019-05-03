package com.grsu.guideapp.fragments.route_preview;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.map.MapFragment;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewFragment;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.TestViews;
import com.grsu.guideapp.models.Route1;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.service.Listener;
import com.grsu.service.LocationClient;
import com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps;
import com.squareup.picasso.Picasso;

public class RoutePreviewFragment extends MapPreviewFragment<RoutePreviewPresenter> implements TestViews,
        OnClickListener, Listener, OnGlobalLayoutListener {

    private static final String TAG = RoutePreviewFragment.class.getSimpleName();
    private BottomSheetBehaviorGoogleMaps behavior;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout fragment;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabActionGo;
    private LocationClient client;
    private Bundle bundle;

    @BindView(R.id.tv_bottom_sheet_title)
    TextView tv_bottom_sheet_title;

    @BindView(R.id.tv_bottom_sheet_description)
    TextView tv_bottom_sheet_description;

    @BindView(R.id.tv_bottom_sheet_route_distance)
    TextView tv_bottom_sheet_route_distance;

    @BindView(R.id.tv_bottom_sheet_route_duration)
    TextView tv_bottom_sheet_route_duration;

    @BindView(R.id.iv_bottom_sheet_route_image)
    ImageView iv_bottom_sheet_route_image;

    @NonNull
    @Override
    protected RoutePreviewPresenter getPresenterInstance() {
        Test helper = new Test(getActivity);
        RoutePreviewInteractor interactor = new RoutePreviewInteractor(helper, getActivity);
        return new RoutePreviewPresenter(this, interactor);
    }

    @Override
    protected String getTitle() {
        return "Test";
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_route_preview;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        String name = getTitle();
        bundle = getArguments();
        if (bundle != null) {
            route = bundle.getInt(Constants.KEY_ID_ROUTE);
            name = bundle.getString(Constants.KEY_NAME_ROUTE);
            LatLng southwest = CryptoUtils.decodeP(bundle.getString(Constants.KEY_SOUTHWEST));
            LatLng northeast = CryptoUtils.decodeP(bundle.getString(Constants.KEY_NORTHEAST));
            bounds = new LatLngBounds(southwest, northeast);
        }

        super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        mPresenter.isDownLoad(route, getString(R.string.locale));
        //mPresenter.isLoadRoute(flag);
        getActivity.setTitleToolbar(name);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        client = new LocationClient.Builder(getContext()).addListener(this).build();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        //getActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
    }

    void initViews() {
        coordinatorLayout = rootView.findViewById(R.id.coordinator_layout);

        fabMyLocation = rootView.findViewById(R.id.fab_my_location);
        fabMyLocation.setOnClickListener(this);
        fabActionGo = rootView.findViewById(R.id.fab_action_go);
        fabActionGo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.actionGoClick(getActivity, route);
            }
        });

        fragment = rootView.findViewById(R.id.map_include);

        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehaviorGoogleMaps.from(bottomSheet);
        behavior.addBottomSheetCallback(mPresenter);
        behavior.setState(BottomSheetBehaviorGoogleMaps.STATE_ANCHOR_POINT);
    }

    @Override
    public void updateViewSize(float offset) {
        LayoutParams params = fragment.getLayoutParams();
        params.height = mPresenter.updateViewSize(offset, bounds);
        fragment.setLayoutParams(params);
    }

    @Override
    public void showBehavior() {
        behavior.setHideable(false);
        behavior.setState(BottomSheetBehaviorGoogleMaps.STATE_COLLAPSED);
    }

    @Override
    public void hideBehavior() {
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehaviorGoogleMaps.STATE_HIDDEN);
    }

    @Override
    public int getBehaviorState() {
        return behavior.getState();
    }

    @Override
    public void updateMyLocationOverlay(Location location) {
        myLocation.setLocation(location);
    }

    @Override
    public void getSingleMyLocation() {
        client.singleConnection();
    }

    @Override
    public int getBehaviorAnchorPoint() {
        return behavior.getAnchorPoint();
    }

    @Override
    public void fabMyLocationScale(float scale) {
        fabMyLocation.setScaleX(scale);
        fabMyLocation.setScaleY(scale);
    }

    @Override
    public void fabActionGoScale(float scale) {
        fabActionGo.setScaleX(scale);
        fabActionGo.setScaleY(scale);
    }

    @Override
    public void fabMyLocationEnabled(boolean isEnabled) {
        fabMyLocation.setEnabled(isEnabled);
    }

    @Override
    public void mapMoveCamera(CameraUpdate cameraUpdate) {
        mMap.moveCamera(cameraUpdate);
    }

    @Override
    public int getCoordinatorLayoutHeight() {
        return coordinatorLayout.getHeight();
    }

    @Override
    public void setFabActionGoImage(int drawable) {
        fabActionGo.setImageResource(drawable);
    }

    @Override
    public float getActionBarSize() {
        return getResources().getDimension(R.dimen.actionBarSize);
    }

    @Override
    public int getBehaviorPeekHeight() {
        return behavior.getPeekHeight();
    }

    public static RoutePreviewFragment newInstance(Bundle args) {
        RoutePreviewFragment fragment = new RoutePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        mPresenter.myLocationClick(getActivity);
    }

    @Override
    public void onChangedLocation(Location location) {
        mPresenter.onChangedLocation(location, bounds, getBorders());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mPresenter.onMapClick(latLng);
    }

    @Override
    public void onGlobalLayout() {
        LayoutParams params = fragment.getLayoutParams();
        params.height = mPresenter.fragmentChangeSize();
        fragment.setLayoutParams(params);
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void mapSettings(boolean flag) {
        if (mMap != null) {
            if (flag) {
                mMap.setOnMapClickListener(this);
            } else {
                mMap.setOnMapClickListener(null);
            }
            mMap.getUiSettings().setScrollGesturesEnabled(flag);
            mMap.getUiSettings().setAllGesturesEnabled(flag);
            mMap.getUiSettings().setTiltGesturesEnabled(flag);
        }
    }

    @Override
    public void setContent(Route1 content) {
        Picasso.get().load(content.getPhotoPath())
                .placeholder(R.drawable.my_location)
                .error(R.drawable.ic_launcher_background)
                .into(iv_bottom_sheet_route_image);

        String duration;
        String distance;
        switch (content.getIdRoute()) {
            case 627: {
                distance = String.format("%s: 3.1 %s", getString(R.string.distance),getString(R.string.short_kilometers));
                duration = String.format("%s: 15 %s",  getString(R.string.duration),getString(R.string.short_minute));
            }
            break;
            case 630: {
                distance = String.format("%s: 49.9 %s",getString(R.string.distance), getString(R.string.short_kilometers));
                duration = String.format("%s: 5 %s"   ,getString(R.string.duration), getString(R.string.short_hour));
            }
            break;
            case 504: {
                distance = String.format("%s: 12 %s",getString(R.string.distance), getString(R.string.short_kilometers));
                duration = String.format("%s: 3 %s" ,getString(R.string.duration), getString(R.string.short_hour));
            }
            break;
            default: {
                duration = toDuration(content.getDuration());
                distance = toDistance(content.getDistance());
            }
        }

        tv_bottom_sheet_title.setText(content.getNameRoute().getFullName());
        tv_bottom_sheet_route_distance.setText(distance);
        tv_bottom_sheet_route_duration.setText(duration);
        tv_bottom_sheet_description.setText(content.getNameRoute().getFullDescription());
    }

    @Override
    public void openMapFragment() {
        getActivity.onReplace(MapFragment.newInstance(bundle));
    }

    private String toDistance(Integer distance) {
        String pattern = "%s: %s %s";
        if (distance > 900) {
            return String.format(pattern, getString(R.string.distance), distance / 1000,
                    getString(R.string.short_kilometers));
        }
        return String.format(pattern, getString(R.string.distance), distance,
                getString(R.string.short_meters));
    }

    private String toDuration(Integer distance) {
        String pattern = "%s: %s %s %s %s";
        if (distance > 60) {
            return String.format(pattern, getString(R.string.duration), distance / 60,
                    getString(R.string.short_hour), distance % 60, getString(R.string.short_minute));
        }
        return String.format(pattern, getString(R.string.duration), distance,
                getString(R.string.short_minute), "", "");
    }
}

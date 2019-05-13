package com.grsu.guideapp.fragments.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.fragments.map_preview.MapPreviewFragment;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.grsu.service.LocationClient;
import com.grsu.service.OnLocationListener;
import java.util.List;

public class MapFragment extends MapPreviewFragment<MapPresenter>
        implements MapViews, OnLocationListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    private boolean isMoving = false;

    private LocationClient client;
    int i = 0;
    Animator animator;

    @BindView(R.id.fragment_map_indicator)
    ImageView found;

    @BindView(R.id.btn_fragment_map_tilt)
    Button btn_fragment_map_tilt;

    @NonNull
    @Override
    protected MapPresenter getPresenterInstance() {
        return new MapPresenter(this, new MapInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.map_fragment);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        String name = getTitle();
        Bundle bundle = getArguments();
        if (bundle != null) {
            route = bundle.getInt(Constants.KEY_ID_ROUTE, -1);
            name = bundle.getString(Constants.KEY_NAME_ROUTE);
            LatLng southwest = CryptoUtils.decodeP(bundle.getString(Constants.KEY_SOUTHWEST));
            LatLng northeast = CryptoUtils.decodeP(bundle.getString(Constants.KEY_NORTHEAST));
            bounds = new LatLngBounds(southwest, northeast);
        }

        super.onCreateView(inflater, container, savedInstanceState);

        client = new LocationClient.Builder(getActivity)
                .setInterval(2000)
                .addListener(this)
                .build();

        getActivity.setTitleToolbar(name);

        //myMovement = DataUtils.getList2();

        if (savedInstanceState != null) {
            i = savedInstanceState.getInt("int", 0);

            if (CheckPermission.canGetLocation(getActivity)) {
                client.connect();
            }
        }

        return rootView;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        map_zoom_control.setVisibility(View.VISIBLE);
        if (CheckPermission.canGetLocation(getActivity)) {
            mMap.setMyLocationEnabled(false);
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    //-------------------------------------------
    //	Summary: implements Contracts
    //-------------------------------------------

    @Override
    public void setCurrentPoint(LatLng latLng) {
        float zoom = mMap.getCameraPosition().zoom;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void show() {
        found.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        found.setVisibility(View.GONE);
    }

    //-------------------------------------------
    //	Summary: implements OnClicks
    //-------------------------------------------

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btn_fragment_map_stop)
    public void stopService(View view) {
        isMoving = false;
        client.disconnect();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btn_fragment_map_start)
    public void startService(View view) {
        if (CheckPermission.canGetLocation(getActivity)) {
            isMoving = true;
            client.connect();
        } else {
            MySnackbar.makeL(view,
                    R.string.error_snackbar_do_not_have_permission_access_location,
                    getActivity);
        }
    }

    @OnClick(R.id.btn_fragment_map_next)
    public void next(View view) {
    }

    @OnClick(R.id.btn_fragment_map_tilt)
    public void tilt(View view) {
        boolean mode = !read(SharedPref.KEY_IS_3D_MODE, Boolean.class);
        save(SharedPref.KEY_IS_3D_MODE, mode);
        Button button = (Button) view;
        float tilt;
        if (mode) {
            button.setText("3D");
            tilt = 0f;
        } else {
            button.setText("2D");
            tilt = 54f;
        }

        mMap.moveCamera(createCamera(myLocation.getMyLocation(Location.class), tilt));
        animator.setTilt(tilt);
    }

    public CameraUpdate createCamera(Location target, float tilt) {
        float zoom = mMap.getCameraPosition().zoom;
        Builder builder = new Builder();
        builder.target(MapUtils.toLatLng(target)).tilt(tilt).zoom(zoom);
        builder.bearing(target.getBearing());
        return CameraUpdateFactory.newCameraPosition(builder.build());
    }

    @Override
    public void onOk() {
        super.onOk();
        mPresenter.getPoi();
    }

    @Override
    public void choiceItem(String itemValue) {
        super.choiceItem(itemValue);
        mPresenter.getPoi();
    }

    private void update(Location currentLocation) {
        List<Point> list = mPresenter.getList(currentLocation);
        LatLng start = list.get(2).getPosition();
        LatLng end = list.get(3).getPosition();

        Location location = MapUtils.toLocation(start);
        float accuracy = MapUtils.getDistanceBetween(currentLocation, MapUtils.toLocation(start));
        location.setAccuracy(accuracy);

        myLocation.setLocation(location);

        animator.startAnimation(start, end);
    }

    public static MapFragment newInstance(Bundle args) {
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onChangedLocation(Location location) {
        if (animator == null){
            btn_fragment_map_tilt.setVisibility(View.VISIBLE);
            animator = new Animator(mMap, myLocation);
        }

        update(location);
        /*if (i < myMovement.size() - 1) {

            LatLng position = myMovement.get(i);
            update(MapUtils.toLocation(position));
            i++;
        } else {
            update(MapUtils.toLocation(myMovement.get(myMovement.size() - 1)));
            client.disconnect();
        }*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("int", i);
        outState.putFloat("zoom", mMap.getCameraPosition().zoom);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        if (CheckPermission.canGetLocation(getActivity) && isMoving){
            client.connect();
            btn_fragment_map_tilt.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStop() {
        if (CheckPermission.canGetLocation(getActivity)){
            client.disconnect();
        }
        if (animator != null) {
            animator.stopAnimation();
        }
        super.onStop();
    }

    @Override
    public void onProviderEnabled(final String var1) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                Logs.e(TAG, "onProviderEnabled: " + var1);
            }
        });
    }

    @Override
    public void onProviderDisabled(final String var1) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                showProgress("Поиск устройства", "Подождите...");
                Logs.e(TAG, "onProviderDisabled: " + var1);
            }
        });
    }

    @Override
    public void onStatusChanged(final String provider, final int status, Bundle bundle) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                Logs.e(TAG, "onStatusChanged: " + provider + "   " + status);
            }
        });
    }
}

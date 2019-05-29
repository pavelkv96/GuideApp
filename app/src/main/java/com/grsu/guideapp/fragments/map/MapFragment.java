package com.grsu.guideapp.fragments.map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
    private boolean isAnimated = false;
    private List<LatLng> myMovement;

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

            if (CheckPermission.checkLocationPermission(getActivity)) {
                client.connect();
                String title = getString(R.string.device_searching);
                String message = getString(R.string.wait_please);
                showProgress(title, message);
            }
        }

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        map_zoom_control.setVisibility(View.VISIBLE);
        if (CheckPermission.checkLocationPermission(getActivity)) {
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
        if (isAnimated) {
            animator.stopAnimation();
            isAnimated = false;
        }
        found.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        if (!isAnimated) {
            isAnimated = true;
        }
        found.setVisibility(View.GONE);
    }

    //-------------------------------------------
    //	Summary: implements OnClicks
    //-------------------------------------------

    @OnClick(R.id.btn_fragment_map_stop)
    public void stopService(View view) {
        isMoving = false;
        if (CheckPermission.checkLocationPermission(getActivity)) {
            client.disconnect();
        }
    }

    @OnClick(R.id.btn_fragment_map_start)
    public void startService(View view) {
        if (CheckPermission.checkLocationPermission(getActivity)) {
            isMoving = true;
            client.connect();
            String title = getString(R.string.device_searching);
            String message = getString(R.string.wait_please);
            showProgress(title, message);
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

        ((Button) view).setText(mode ? "3D" : "2D");
        float tilt = mode ? 0f : 54f;
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
        if (accuracy <= 25 && isAnimated) {
            myLocation.setLocation(location);
            animator.startAnimation(start, end);
        } else {
            myLocation.setLocation(currentLocation);
            LatLng latLng = MapUtils.toLatLng(currentLocation);
            try {
                LatLngBounds latLngBounds = MapUtils.getBounds(latLng, bounds, getBorders());
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
            } catch (NullPointerException e) {
                hideProgress();
                if (CheckPermission.checkLocationPermission(getActivity))
                client.disconnect();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity);
                dialog.setTitle(R.string.warning);
                dialog.setMessage(R.string.you_are_out_of_map);
                dialog.setCancelable(false);
                dialog.setPositiveButton(R.string.click_to_exit, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity.getSupportFragmentManager().popBackStack();
                    }
                });
                dialog.create();
                dialog.show();
            }
        }
    }

    public static MapFragment newInstance(Bundle args) {
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onChangedLocation(Location location) {
        hideProgress();
        if (animator == null) {
            btn_fragment_map_tilt.setVisibility(View.VISIBLE);
            animator = new Animator(mMap, myLocation);
            isAnimated = true;
        }

        update(location);
        /*if (i < myMovement.size() - 1) {

            LatLng position = myMovement.get(i);
            update(MapUtils.toLocation(position));
            i++;
        } else {
            update(MapUtils.toLocation(myMovement.get(myMovement.size() - 1)));
            if (CheckPermission.checkLocationPermission(getActivity)) {
                client.disconnect();
            }
        }*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("int", i);
        outState.putFloat("zoom", mMap.getCameraPosition().zoom);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CheckPermission.checkLocationPermission(getActivity) && isMoving) {
            client.connect();
            String title = getString(R.string.device_searching);
            String message = getString(R.string.wait_please);
            showProgress(title, message);

            Boolean mode = read(SharedPref.KEY_IS_3D_MODE, Boolean.class);
            btn_fragment_map_tilt.setText(mode ? "3D" : "2D");
            btn_fragment_map_tilt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        if (CheckPermission.checkLocationPermission(getActivity)) {
            client.disconnect();
        }
        if (animator != null) {
            isAnimated = false;
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
                String title = getString(R.string.device_searching);
                String message = getString(R.string.wait_please);
                showProgress(title, message);
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

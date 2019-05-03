package com.grsu.guideapp.fragments.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.fragments.map_preview.MapPreviewFragment;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.grsu.service.Listener;
import com.grsu.service.LocationClient;
import java.util.List;

public class MapFragment extends MapPreviewFragment<MapPresenter>
        implements MapViews, Listener, OnSeekBarChangeListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    //private List<LatLng> myMovement;//deleting

    private LocationClient client;
    int i = 0;
    Animator animator;

    @BindView(R.id.sb)
    SeekBar seekBar;

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

        client = new LocationClient.Builder(getActivity).setInterval(2000).addListener(this)
                .build();

        seekBar.setMax(68);
        seekBar.setOnSeekBarChangeListener(this);

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

    @Override
    public void onStop() {
        super.onStop();
        Logs.e(TAG, "onStop");
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

    @OnClick(R.id.btn_fragment_map_stop)
    public void stopService(View view) {
        client.disconnect();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btn_fragment_map_start)
    public void startService(View view) {
        if (CheckPermission.canGetLocation(getActivity)) {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
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
        Button button = (Button) view;
        String text = button.getText().toString();
        float tilt;
        if (text.equals("2D")) {
            button.setText("3D");
            tilt = 0f;
        } else {
            button.setText("2D");
            tilt = 54f;
        }

        mMap.moveCamera(createCamera(myLocation.getMyLocation(), tilt));
        animator.setTilt(tilt);
    }

    public CameraUpdate createCamera(Location target, float tilt) {
        float zoom = mMap.getCameraPosition().zoom;
        Builder builder = new Builder();
        builder.target(MapUtils.toLatLng(target)).tilt(tilt).zoom(zoom);
        return CameraUpdateFactory.newCameraPosition(builder.build());
    }

    public CameraUpdate createCamera(Location target, Location next, float tilt) {
        float zoom = mMap.getCameraPosition().zoom;
        Builder builder = new Builder();
        builder.target(MapUtils.toLatLng(target)).tilt(tilt).zoom(zoom);
        builder.bearing(target.bearingTo(next));
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        List<Point> list = mPresenter.getList(myLocation.getMyLocation());
        mMap.moveCamera(createCamera(MapUtils.toLocation(list.get(2).getPosition()),
                MapUtils.toLocation(list.get(3).getPosition()), i));
        Log.e(TAG, "onProgressChanged: " + i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroy() {
        client.disconnect();
        super.onDestroy();
    }
}

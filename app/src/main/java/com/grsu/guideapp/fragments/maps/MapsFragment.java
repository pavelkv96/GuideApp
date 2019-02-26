package com.grsu.guideapp.fragments.maps;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewFragment;
import com.grsu.guideapp.fragments.maps.MapsContract.MapViews;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.DataUtils;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import java.util.List;

public class MapsFragment extends MapPreviewFragment<MapsPresenter>
        implements MapViews, OnMyLocationChangeListener {

    private static final String TAG = MapsFragment.class.getSimpleName();
    private List<LatLng> myMovement;//deleting

    private Marker marker;//deleting
    private Marker current;
    int i = 0;
    Animator animator;

    @BindView(R.id.fragment_map_indicator)
    ImageView found;

    @NonNull
    @Override
    protected MapsPresenter getPresenterInstance() {
        return new MapsPresenter(this, new MapsInteractor(new DatabaseHelper(getContext())));
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

        getActivity.setTitleToolbar(name);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMovement = DataUtils.getList2();
        super.onMapReady(googleMap);
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
        if (current == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            current = mMap.addMarker(markerOptions);
        }
        float zoom = mMap.getCameraPosition().zoom;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        current.setPosition(latLng);
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
        mMap.setOnMyLocationChangeListener(null);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btn_fragment_map_start)
    public void startService(View view) {
        if (!CheckSelfPermission.getAccessLocationIsGranted(getContext())) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(this);
        } else {
            MySnackbar.makeL(view,
                    R.string.error_snackbar_do_not_have_permission_access_fine_location,
                    getActivity);
        }
    }

    @OnClick(R.id.btn_fragment_map_next)
    public void next(View view) {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        Log.e(TAG, "next: " + bounds);
        /*if (i < myMovement.size() - 1) {

            LatLng position = myMovement.get(i);
            if (i == 0) {
                MarkerOptions markerOptions = new MarkerOptions().position(position);
                float hueGreen = BitmapDescriptorFactory.HUE_GREEN;
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hueGreen));
                marker = mMap.addMarker(markerOptions);
                current = mMap.addMarker(markerOptions);
                animator = new Animator(mMap, current);
            }
            marker.setPosition(position);
            mPresenter.getProjectionLocation(MapUtils.toLocation(position));
            i++;
        } else {
            Location currentLocation = MapUtils.toLocation(myMovement.get(myMovement.size() - 1));
            mPresenter.getProjectionLocation(currentLocation);
        }*/
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

    @Override
    public void onMyLocationChange(Location location) {
        //listener.onLocationChanged(location);
        if (i < myMovement.size() - 1) {

            LatLng position = myMovement.get(i);
            if (i == 0) {
                MarkerOptions markerOptions = new MarkerOptions().position(position);
                float hueGreen = BitmapDescriptorFactory.HUE_GREEN;
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hueGreen));
                //marker = mMap.addMarker(markerOptions);
                current = mMap.addMarker(markerOptions);
                animator = new Animator(mMap, current);
            }
            //marker.setPosition(position);
            //mPresenter.getProjectionLocation(MapUtils.toLocation(position));

            update(MapUtils.toLocation(position));
            i++;
        } else {
            Location currentLocation = MapUtils.toLocation(myMovement.get(myMovement.size() - 1));
            //mPresenter.getProjectionLocation(currentLocation);
            update(currentLocation);
            mMap.setOnMyLocationChangeListener(null);
        }
    }

    private void update(Location currentLocation) {
        List<Point> list = mPresenter.getList(currentLocation);
        LatLng start = list.get(2).getPosition();
        LatLng end;
        if (start != list.get(3).getPosition()) {
            end = list.get(3).getPosition();
        } else {
            end = list.get(4).getPosition();
        }

        animator.startAnimation(start, end);
    }

    public static MapsFragment newInstance(Bundle args) {
        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

package com.grsu.guideapp.fragments.route_preview;

import android.content.Context;
import android.location.Location;
import android.support.annotation.DrawableRes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnLoadRoute;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewInteractor;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewPresenter;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Route1;

public interface RoutePreviewContract {

    interface TestViews extends MapPreviewViews {

        float getActionBarSize();

        int getBehaviorAnchorPoint();

        int getBehaviorPeekHeight();

        int getBehaviorState();

        int getCoordinatorLayoutHeight();

        void fabActionGoScale(float scale);

        void fabMyLocationEnabled(boolean isEnabled);

        void fabMyLocationScale(float scale);

        void getSingleMyLocation();

        void hideBehavior();

        void mapMoveCamera(CameraUpdate cameraUpdate);

        void setFabActionGoImage(@DrawableRes int drawable);

        void showBehavior();

        void updateMyLocationOverlay(Location location);

        void updateViewSize(float offset);

        void requestPermissions(String[] permissions, int requestCode);

        void mapSettings(boolean isUsable);

        void setContent(Route1 content);

        void openMapFragment();
    }

    interface TestPresenter extends MapPreviewPresenter {

        void isLoadRoute(int isLoad);

        void onMapClick(LatLng latLng);

        void onChangedLocation(Location location, LatLngBounds routeBounds, LatLngBounds mapBounds);

        void myLocationClick(Context context);

        void actionGoClick(Context context, int id_route);

        void onRequestPermissionsResult(int requestCode, int[] grantResults);

        int fragmentChangeSize();

        int updateViewSize(float offset, LatLngBounds bounds);

        void isDownLoad(int id_route, String locale);
    }

    interface TestInteractor extends MapPreviewInteractor {

        void isDownLoad(OnFinishedListener<Route1> listener, int id_route, String locale);

        void loadRoute(OnLoadRoute<String> listener, int id_route);

        void setFlag(Boolean flag);
    }
}

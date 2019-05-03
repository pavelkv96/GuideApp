package com.grsu.guideapp.fragments.route_preview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnLoadRoute;
import com.grsu.guideapp.database.Table;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewPresenter;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.TestViews;
import com.grsu.guideapp.models.Route1;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.ui.bottomsheet.BottomSheetBehaviorGoogleMaps;
import com.grsu.ui.bottomsheet.BottomSheetCallback;

public class RoutePreviewPresenter extends MapPreviewPresenter implements
        RoutePreviewContract.TestPresenter,
        BottomSheetCallback, OnLoadRoute<String> {

    private static final String TAG = RoutePreviewPresenter.class.getSimpleName();
    private boolean flag = false;
    private int isLoadRoute = 0;
    private ProgressDialog mProgressDialog;

    private TestViews testViews;
    private RoutePreviewInteractor routePreviewInteractor;

    RoutePreviewPresenter(TestViews mapViews, RoutePreviewInteractor mapInteractor) {
        super(mapViews, mapInteractor);
        testViews = mapViews;
        routePreviewInteractor = mapInteractor;
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
                testViews.mapSettings(false);

            }
            break;
            case BottomSheetBehaviorGoogleMaps.STATE_EXPANDED: {
                Log.e(TAG, "onStateChanged: expanded");
            }
            break;
            case BottomSheetBehaviorGoogleMaps.STATE_COLLAPSED: {
                Log.e(TAG, "onStateChanged: collapsed");
                testViews.mapSettings(true);
            }
            break;
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (slideOffset <= .5032f) {
            Log.e(TAG, "onSlide: " + slideOffset);
            testViews.updateViewSize(slideOffset);
        }
    }

    @Override
    public void isLoadRoute(int isLoad) {
        isLoadRoute = isLoad;
        int imageRes = isLoadRoute > 0 ? R.drawable.ic_action_go : R.drawable.ic_download;
        testViews.setFabActionGoImage(imageRes);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (flag) {
            flag = false;
            testViews.showBehavior();
        } else {
            flag = true;
            testViews.hideBehavior();
        }
    }

    @Override
    public void onChangedLocation(Location loc, LatLngBounds routeBounds, LatLngBounds mapBound) {
        try {
            LatLng latLng = MapUtils.toLatLng(loc);
            LatLngBounds newBounds = MapUtils.getBounds(latLng, routeBounds, mapBound);
            flag = true;

            testViews.fabMyLocationEnabled(true);
            testViews.mapMoveCamera(CameraUpdateFactory.newLatLngBounds(newBounds, 50));
            testViews.updateMyLocationOverlay(loc);
            testViews.hideBehavior();

        } catch (NullPointerException ignore) {
            String message = "Сообщение о том что откроется диалоговое окно о том что пользователь вне карты";
            testViews.fabMyLocationEnabled(true);
            testViews.mapMoveCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 20));
            testViews.showToast(message);
        }
    }

    @Override
    public void myLocationClick(Context context) {
        if (CheckPermission.canGetLocation(context)) {
            testViews.fabMyLocationEnabled(false);
            testViews.getSingleMyLocation();
        } else {
            testViews.requestPermissions(CheckPermission.groupLocation, 1);
        }
    }

    @Override
    public void actionGoClick(Context context, int id_route) {
        if (isLoadRoute == Table.NOT_DOWNLOAD) {
            if (CheckPermission.canWriteStorage(context)) {
                if (App.isOnline()) {
                    onStartLoad(id_route);
                } else {
                    testViews.showToast(context.getString(R.string.no_internet_connection));
                }
            } else {
                testViews.requestPermissions(CheckPermission.groupStorage, 2);
            }
        } else {
            if (CheckPermission.canGetLocation(context)) {
                testViews.openMapFragment();
            } else {
                testViews.requestPermissions(CheckPermission.groupLocation, 3);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (CheckPermission.isAllGranted(grantResults)) {
            if (requestCode == 1) {
                testViews.fabMyLocationEnabled(false);
                testViews.getSingleMyLocation();
            }
            if (requestCode == 3) {
                testViews.openMapFragment();
            }
        } else {
            testViews.showToast(R.string.error_snackbar_do_not_have_permission_access_location);
        }
    }

    @Override
    public int fragmentChangeSize() {
        int top = (int) testViews.getActionBarSize();
        int point = 0;
        if (testViews.getBehaviorState() == BottomSheetBehaviorGoogleMaps.STATE_COLLAPSED) {
            testViews.mapSettings(true);
            testViews.fabMyLocationScale(1);
        } else {
            testViews.mapSettings(false);
            point = testViews.getBehaviorAnchorPoint();
        }
        int coordinatorLayoutHeight = testViews.getCoordinatorLayoutHeight();
        int behaviorPeekHeight = testViews.getBehaviorPeekHeight();
        return coordinatorLayoutHeight - point - behaviorPeekHeight - top;
    }

    @Override
    public int updateViewSize(float offset, LatLngBounds bounds) {
        int max = testViews.getCoordinatorLayoutHeight();
        float top = testViews.getActionBarSize();
        int behaviorHeight = testViews.getBehaviorPeekHeight();
        int height = (int) (max - max * offset - behaviorHeight * (1 - offset) - top);
        if (offset <= .5032f && offset >= 0) {
            testViews.fabMyLocationScale(offset / .5f - 1);
            if (offset > 0) {
                testViews.mapMoveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
            }
        }
        if (offset < 0) {
            height = (int) (max - behaviorHeight * (1 + offset) - top);
            testViews.fabMyLocationScale(1 + offset);
            testViews.fabActionGoScale(1 + offset);
        }

        return height;
    }

    @Override
    public void isDownLoad(int id_route, String locale) {
        routePreviewInteractor.isDownLoad(new OnFinishedListener<Route1>() {
            @Override
            public void onFinished(final Route1 route) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoadRoute(route.getIsFull());
                        testViews.setContent(route);
                    }
                });
            }
        }, id_route, locale);
    }

    private void showProgress(String title, String message) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(testViews.getContentView().getContext());

            mProgressDialog.setTitle(title);

            mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel",
                    (DialogInterface.OnClickListener) null);

            mProgressDialog.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    final ProgressDialog dialog = (ProgressDialog) dialogInterface;
                    final Button button = dialog.getButton(ProgressDialog.BUTTON_NEGATIVE);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.setMessage("Сanceled download...");
                            button.setVisibility(View.GONE);
                            routePreviewInteractor.setFlag(false);
                        }
                    });
                }
            });
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onSuccess(final String s) {
        isLoadRoute = Table.DOWNLOAD;
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                testViews.setFabActionGoImage(R.drawable.ic_action_go);
                testViews.showToast(s);
            }
        });
    }

    @Override
    public void onStartLoad(int id_route) {
        routePreviewInteractor.loadRoute(this, id_route);
        showProgress("Load route", "Pleas wait...");
    }

    @Override
    public void onFailure(final Throwable throwable) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                testViews.showToast(throwable.getMessage() + "\nRetry again later");
            }
        });
    }

    @Override
    public void onCancelLoad() {
        hideProgress();
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                testViews.showToast(android.R.string.cancel);
            }
        });
    }
}

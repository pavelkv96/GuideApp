package com.grsu.guideapp.fragments.setting;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;

public abstract class BaseSampleFragment extends Fragment {

    public static final String TAG = "osmBaseFrag";

    protected void addOverlays() {
    }

    protected MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Logs.e(TAG, "onCreateView");
        mMapView = new MapView(inflater.getContext());
        return mMapView;
    }


    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logs.e(TAG, "onActivityCreated");

        if (mMapView != null) {
            addOverlays();

            if (getActivity() != null) {
                CopyrightOverlay copyrightOverlay = new CopyrightOverlay(getActivity());
                copyrightOverlay.setTextSize(10);
                mMapView.getOverlays().add(copyrightOverlay);
            }

            mMapView.setBuiltInZoomControls(true);
            mMapView.setMultiTouchControls(true);
            mMapView.setUseDataConnection(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logs.e(TAG, "onDetach");
        if (mMapView != null) {
            mMapView.onDetach();
        }
        mMapView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logs.e(TAG, "onDestroy");
    }
}

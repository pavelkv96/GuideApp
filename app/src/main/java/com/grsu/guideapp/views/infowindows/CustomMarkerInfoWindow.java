package com.grsu.guideapp.views.infowindows;

import android.support.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.View;
import com.grsu.guideapp.R;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public final class CustomMarkerInfoWindow extends CustomBasicInfoWindow {

    private static final String TAG = CustomMarkerInfoWindow.class.getSimpleName();

    private static Marker mMarkerRef;

    public CustomMarkerInfoWindow(MapView mapView, boolean isVisible) {
        this(R.layout.bubble, mapView, isVisible);
    }

    public CustomMarkerInfoWindow(@LayoutRes int layoutRes, MapView mapView, boolean isVisible) {
        super(layoutRes, mapView, isVisible);
    }

    public static Marker getMarkerReference() {
        return mMarkerRef;
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        mMarkerRef = (Marker) item;
        if (mView == null) {
            throw new NullPointerException("Error trapped, MarkerInfoWindow.open, mView is null!");
        }

        mView.setOnTouchListener(this);
    }

    @Override
    public void onClose() {
        super.onClose();
        mMarkerRef = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        view.performClick();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mView.setBackgroundResource(R.drawable.bubble_red);
            }
            break;

            case MotionEvent.ACTION_UP: {
                //close();
                //Toasts.makeS(ContextHolder.getContext(), "Open new activity");
                mView.setBackgroundResource(R.drawable.bubble_white);
            }
            break;

        }

        return true;
    }
}

package com.grsu.guideapp.views.infowindows;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.grsu.guideapp.R;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomBasicInfoWindow extends InfoWindow implements OnTouchListener {

    private static final String TAG = CustomBasicInfoWindow.class.getSimpleName();

    public CustomBasicInfoWindow(MapView mapView) {
        this(R.layout.bubble, mapView);
    }

    public CustomBasicInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);

        mView.setOnTouchListener(this);
    }

    @Override
    public void onOpen(Object item) {
        //OverlayWithIW overlay = (OverlayWithIW) item;
        if (mView == null) {
            throw new NullPointerException("Error trapped, BasicInfoWindow.open, mView is null!");
        }
        //Logic for standard view
    }

    @Override
    public void onClose() {
        //by default, do nothing
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        view.performClick();
        /*switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {

            }
            break;
        }*/

        return true;
    }
}

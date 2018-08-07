package com.grsu.guideapp.views.infowindows;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.ContextHolder;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public final class CustomMarkerInfoWindow extends CustomBasicInfoWindow implements OnTouchListener {

    private static final String TAG = CustomMarkerInfoWindow.class.getSimpleName();
    Marker marker;

    private static Marker mMarkerRef; //reference to the Marker on which it is opened. Null if none.

    public CustomMarkerInfoWindow(MapView mapView) {
        super(R.layout.bubble, mapView);
        //mMarkerRef = null;
    }

    /**
     * reference to the Marker on which it is opened. Null if none.
     */
    public static Marker getMarkerReference() {
        return mMarkerRef;
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);

        mMarkerRef = (Marker) item;
        if (mView == null) {
            Logs.e(TAG, "Error trapped, MarkerInfoWindow.open, mView is null!");
            return;
        }
        //handle image
        ImageView imageView = mView.findViewById(mImageId /*R.id.image*/);
        Drawable image = mMarkerRef.getImage();
        if (image != null) {
            imageView.setImageDrawable(image); //or setBackgroundDrawable(image)?
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        marker = (Marker) item;
        mView.setOnTouchListener(this);
    }

    @Override
    public void onClose() {
        super.onClose();
        mMarkerRef = null;
        //by default, do nothing else
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                view.performClick();
                if (marker != null) {
                    //close();
                    Logs.e(TAG, "onOpen2: " + marker.getPosition());
                    Toasts.makeS(ContextHolder.getContext(), "Open new activity");
                }
                break;
        }

        return true;
    }
}

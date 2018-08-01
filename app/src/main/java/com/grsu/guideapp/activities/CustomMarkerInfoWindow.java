package com.grsu.guideapp.activities;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsActivity;
import com.grsu.guideapp.utils.ContextHolder;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class CustomMarkerInfoWindow extends MarkerInfoWindow implements OnTouchListener {

    Marker marker;

    public CustomMarkerInfoWindow(MapView mapView) {
        super(R.layout.bubble, mapView);
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        marker = (Marker) item;

        Button button = mView.findViewById(R.id.bubble_moreinfo);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MarkerSingleton", "onOpen: " + marker.getPosition());
            }
        });

        mView.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                view.performClick();
                if (marker != null) {
                    //close();
                    Log.e("MarkerSingleton", "onOpen2: " + marker.getPosition());
                    ContextHolder.getContext().startActivity(new Intent(ContextHolder.getContext(), DetailsActivity.class));
                }
                break;
        }

        return true;
    }
}

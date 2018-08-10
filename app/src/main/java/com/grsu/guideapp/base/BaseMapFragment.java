/*
package com.grsu.guideapp.base;

import static com.grsu.guideapp.utils.CheckSelfPermission.writeExternalStorageIsGranted;

import android.content.Context;
import android.support.annotation.NonNull;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.PolylineSingleton;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;

public class BaseMapFragment extends BaseFragment<P>
        implements MapEventsReceiver, OnMarkerClickListener {

    PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;
    MarkerSingleton markerSingleton = MarkerSingleton.Marker;

    @BindView(R.id.mv_fragment_map)
    MapView mapView;

    @Override
    public void onAttach(@NonNull Context context) {
        if (writeExternalStorageIsGranted(context)) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
        super.onAttach(context);
    }

    @NonNull
    @Override
    protected P getPresenterInstance() {
        return new P();
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        return false;
    }
}
*/

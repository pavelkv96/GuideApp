package com.grsu.guideapp.activities.route;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class RoutePresenter extends BasePresenterImpl<RouteView> implements OnFinishedListener,
        RouteContract.RoutePresenter {

    private static final String TAG = RoutePresenter.class.getSimpleName();

    private RouteView routeView;
    private RouteInteractor routeInteractor;

    public RoutePresenter(RouteView routeView, RouteInteractor routeInteractor) {
        this.routeView = routeView;
        this.routeInteractor = routeInteractor;
    }

    @Override
    public void getId(Integer id) {
        mView.showProgress(null, "Loading");
        routeInteractor.getRouteById(this, id);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint, MapView mapView) {
        InfoWindow.closeAllInfoWindowsOn(mapView);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        InfoWindow.closeAllInfoWindowsOn(mapView);
        mapView.getController().animateTo(marker.getPosition());
        marker.showInfoWindow();
        routeInteractor.getPoiById(this, marker.getPosition());

        return true;
    }

    @Override
    public boolean onClickPolyline(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        InfoWindow.closeAllInfoWindowsOn(mapView);
        mapView.getController().animateTo(eventPos);
        polyline.setInfoWindowLocation(eventPos);
        polyline.showInfoWindow();
        return true;
    }

    @Override
    public void getMarkers() {
        getMarkersWithSettings(null);
    }

    @Override
    public void getMarkersWithSettings(List<Integer> typesObjects) {
        routeView.removeMarker();
        routeInteractor.getListPoi(this, 0.0, 0.0, 0, typesObjects);
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        try {
            Line line = null;
            for (Line encodePolyline : encodePolylines) {
                routeView.setPolyLine(decodeL(encodePolyline.getPolyline()));
                routeView.setPoints(decodeP(encodePolyline.getStartPoint()));
                line = encodePolyline;
            }
            routeView.setPoints(decodeP(line.getEndPoint()));
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }

    @Override
    public void onFinished1(List<Poi> poiList) {
        //routeView.removeMarker();

        for (Poi poi : poiList) {
            routeView.setGetPoints(poi);
            Logs.e(TAG, poi.getId() + "; " + poi.getLatitude() + "; " + poi.getLongitude());
        }
    }

    @Override
    public void attachView(RouteView view) {
        super.attachView(view);
        routeView.removeMarker();
    }
}
package com.grsu.guideapp.activities.route;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
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
    private List<Integer> types = new ArrayList<>();
    private Integer checkedItem;
    private GeoPoint mGeoPoint;

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
    public boolean singleTapConfirmedHelper(GeoPoint pGeoPoint, MapView mapView) {
        InfoWindow.closeAllInfoWindowsOn(mapView);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint pGeoPoint) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        InfoWindow.closeAllInfoWindowsOn(mapView);
        mapView.getController().animateTo(marker.getPosition());
        marker.showInfoWindow();
        mGeoPoint = marker.getPosition();
        getMarkers(mGeoPoint);
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
    public void getMarkers(GeoPoint pGeoPoint) {
        if (pGeoPoint != null) {
            getMarkersWithSettings(pGeoPoint);
            return;
        }
        if (mGeoPoint != null) {
            getMarkersWithSettings(mGeoPoint);
        }

    }

    @Override
    public void setRadius(String radius) {
        checkedItem = Integer.valueOf(radius);
    }

    @Override
    public void setType(List<Integer> typesObjects) {
        types = typesObjects;
    }

    @Override
    public List<Integer> getType() {
        return types;
    }

    @Override
    public void getMarkersWithSettings(GeoPoint pGeoPoint) {
        cleanUpMap();
        if (types != null && types.size() > 0) {
            routeInteractor.getListPoi(
                    this, pGeoPoint.getLatitude(), pGeoPoint.getLongitude(), checkedItem, types
            );
        }
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        try {
            Line line = null;
            for (Line encodePolyline : encodePolylines) {
                routeView.setPolyline(decodeL(encodePolyline.getPolyline()));
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
        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(mGeoPoint);
        for (Poi poi : poiList) {
            geoPointList.add(new GeoPoint(poi.getLatitude(), poi.getLongitude()));
            routeView.setGetPolyline(geoPointList);
            geoPointList.remove(geoPointList.size() - 1);
            routeView.setGetPoints(poi);
            Logs.e(TAG, poi.getId() + "; " + poi.getLatitude() + "; " + poi.getLongitude());
        }
    }

    @Override
    public void attachView(RouteView view) {
        super.attachView(view);
        cleanUpMap();
    }

    private void cleanUpMap() {
        routeView.removeMarker();
        routeView.removePolylines();
    }
}
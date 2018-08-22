package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.map.MapContract.MapInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.LineG;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MapPresenter extends BasePresenterImpl<MapViews> implements MapContract.MapPresenter,
        OnFinishedListener {

    private static final String TAG = MapPresenter.class.getSimpleName();
    private static final Integer RADIUS = 100;
    private List<LineG> markers = new ArrayList<>();
    private Marker currentMarker;
    private LineG currentLine;

    private MapViews mapViews;
    private MapInteractor mapInteractor;

    public MapPresenter(MapViews mapViews, MapInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void getId(Integer id) {
        mView.showProgress(null, "Loading...");
        mapInteractor.getRouteById(this, id);
    }

    @Override
    public void getLocation(Location currentLocation) {

        if (currentMarker != null) {
            GeoPoint position = currentMarker.getPosition();

            if (getDistanceBetween(currentLocation, position) > RADIUS) {
                currentMarker = null;
                Logs.e(TAG, "REMOVE");
                mapViews.removeMarkers();
            }
        }

        if (currentLine != null) {
            GeoPoint point = currentLine.getStartPoint().getPosition();

            getCurrentTurn(currentLocation, toLocation(point));
        } else {
            mapViews.stopped();
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //getLocation(toLocation(p));
        //Logs.e(TAG, currentLine.toString());
        return false;
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        try {
            for (Line encodeLine : encodePolylines) {
                Integer idLine = encodeLine.getIdLine();

                mapViews.setPolyline(decodeL(encodeLine.getPolyline()), idLine);

                Marker startMarker = mapViews.setPoints(decodeP(encodeLine.getStartPoint()));
                Marker endMarker = mapViews.setPoints(decodeP(encodeLine.getEndPoint()));

                LineG line = new LineG(idLine, startMarker, endMarker, "dfdsf");
                if (currentMarker == null) {
                    currentMarker = startMarker;
                    currentLine = line;
                }

                markers.add(line);
            }
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }

    @Override
    public void onFinished1(List<Poi> poiList) {
        mapViews.removeMarkers();
        for (Poi poi : poiList) {
            mapViews.setGetPoints(poi);
        }
    }

    //--------------------------------------------------------------------------------------------

    private void getCurrentTurn(Location currentLocation, Location newLocation) {
        GeoPoint endPosition = currentLine.getEndPoint().getPosition();
        Location endLocation = toLocation(endPosition);

        float startDistance = currentLocation.distanceTo(newLocation); //in meters
        float endDistance = currentLocation.distanceTo(endLocation); //in meters

        Logs.e(TAG, "DISTANCE " + isMoreDistance(startDistance, endDistance) + "  " +
                startDistance + "   " + endDistance);

        if (isMoreDistance(startDistance, endDistance)) {

            if (endDistance <= RADIUS) {
                currentMarker = currentLine.getEndPoint();

                Logs.e(TAG, "QUERY 1");
                mapInteractor.getListPoi(
                        this,
                        endPosition.getLatitude(),
                        endPosition.getLongitude(),
                        1000,
                        getList());
            }

            currentLine = nextPolyline(currentLine.getIdLine());
        } else {
            if (currentMarker == null && startDistance <= RADIUS) {
                currentMarker = currentLine.getStartPoint();

                GeoPoint position = currentMarker.getPosition();

                Logs.e(TAG, "QUERY");
                mapInteractor.getListPoi(
                        this, position.getLatitude(), position.getLongitude(), 1000, getList());
            }
        }
    }


    /**
     * Finding nearest turn for current position in relation to the route
     *
     * @return startLine
     */
    @Nullable
    private LineG nextPolyline(Integer id) {
        Logs.e(TAG, "LINE " + id);
        if (id < markers.size()) {
            return markers.get(id);
        }

        return null;
    }

    private List<Integer> getList() {
        //TODO it is multi choice response
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        return integers;
    }

    /**
     * Finding nearest turn for current position in relation to the route
     *
     * @return startLine
     */
    @NonNull
    private LineG findNearestTurn(Location currentLocation) {
        LineG startLine = markers.get(0);
        LineG endLine = markers.get(markers.size());

        GeoPoint startPosition = startLine.getStartPoint().getPosition();

        Float distance = getDistanceBetween(currentLocation, startPosition);

        for (LineG lineG : markers) {
            if (isMoreDistance(distance, currentLocation, lineG)) {
                distance = getDistanceBetween(currentLocation, lineG.getStartPoint().getPosition());
                startLine = lineG;
            }
        }

        if (isMoreDistance(distance, currentLocation, endLine)) {
            startLine = endLine;
        }

        return startLine;
    }

    private boolean isMoreDistance(float startDistance, float endLocation) {
        return startDistance > endLocation;
    }

    private boolean isMoreDistance(float distance, Location currentLocation, LineG lineG) {
        return distance > getDistanceBetween(currentLocation, lineG.getStartPoint().getPosition());
    }

    public Location toLocation(GeoPoint position) {
        Location location = new Location("");
        location.setLatitude(position.getLatitude());
        location.setLongitude(position.getLongitude());
        return location;
    }

    public static GeoPoint toGeoPoint(Location location) {
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    private float getDistanceBetween(GeoPoint currentPosition, GeoPoint newPosition) {
        Location startLocation = new Location("currentLocation");
        startLocation.setLatitude(currentPosition.getLatitude());
        startLocation.setLongitude(currentPosition.getLongitude());

        return getDistanceBetween(startLocation, newPosition);
    }

    private float getDistanceBetween(Location startLocation, GeoPoint newPosition) {

        Location endLocation = new Location("newsLocation");
        endLocation.setLatitude(newPosition.getLatitude());
        endLocation.setLongitude(newPosition.getLongitude());

        return startLocation.distanceTo(endLocation);
    }
}

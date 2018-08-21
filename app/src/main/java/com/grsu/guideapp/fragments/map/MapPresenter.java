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
    private List<LineG> markers = new ArrayList<>();
    private Marker currentMarker;
    private LineG currentLine;
    private boolean i = true;

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
        if (currentLine != null) {
            GeoPoint point = currentLine.getStartPoint().getPosition();

            Location newLocation = new Location("newlocation");
            newLocation.setLatitude(point.getLatitude());
            newLocation.setLongitude(point.getLongitude());

            getCurrentTurn(currentLocation, newLocation);
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        if (i) {
            getLocation(toLocation(p));
        }
        //Logs.e(TAG, currentLine.toString());
        return false;
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        try {
            for (Line encodeLine : encodePolylines) {
                Integer idLine = encodeLine.getIdLine();
                Marker startMarker = mapViews.setPoints(decodeP(encodeLine.getStartPoint()));
                Marker endMarker = mapViews.setPoints(decodeP(encodeLine.getEndPoint()));

                mapViews.setPolyline(decodeL(encodeLine.getPolyline()), idLine);

                LineG line = new LineG(idLine, startMarker, endMarker, "dfdsf");
                if (currentMarker == null) {
                    currentMarker = startMarker;
                    currentLine = line;

                    Logs.e(TAG, currentLine.toString());
                }

                markers.add(line);
            }
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }

    @Override
    public void onFinished1(List<Poi> poiList) {
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

        Logs.e(TAG, "DISTANCE " +
                currentLine.getIdLine() + "  " +
                isMoreDistance(startDistance, endDistance) + "  " +
                startDistance + "   " + endDistance);

        if (isMoreDistance(startDistance, endDistance)) {

            //TODO it is multi choice response
            List<Integer> integers = new ArrayList<>();
            integers.add(1);
            integers.add(2);
            integers.add(3);
            integers.add(4);

            if (endDistance <= 100) {
                mapInteractor.getListPoi(
                        this,
                        endPosition.getLatitude(),
                        endPosition.getLongitude(),
                        1000,
                        integers);
            }

            currentLine = nextPolyline(currentLine.getIdLine());
            if (currentLine == null) {
                // Receiver unregistered
                i = false;
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

    private GeoPoint toGeoPoint(Location location) {
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    private float getDistanceBetween(GeoPoint currentPosition, GeoPoint newPosition) {
        Location startLocation = new Location("currentLocation");
        startLocation.setLatitude(currentPosition.getLatitude());
        startLocation.setLongitude(currentPosition.getLongitude());

        return getDistanceBetween(startLocation, newPosition);
    }

    private float getDistanceBetween(Location startLocation, GeoPoint newPosition) {

        Location endLocation = new Location("newLocation");
        endLocation.setLatitude(newPosition.getLatitude());
        endLocation.setLongitude(newPosition.getLongitude());

        return startLocation.distanceTo(endLocation);
    }
}

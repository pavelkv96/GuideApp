package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import android.graphics.Color;
import android.location.Location;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.map.MapContract.MapInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

public class MapPresenter extends BasePresenterImpl<MapViews> implements MapContract.MapPresenter,
        OnFinishedListener {

    private static final Integer RADIUS = 100;
    private static final String TAG = MapPresenter.class.getSimpleName();
    private GeoPoint currentGeoPoint;
    private List<GeoPoint> geoPoints;
    private Integer currentIndex;
    private List<GeoPoint> allGeoPoints;
    private Polyline polyline;
    private List<GeoPoint> turnsList;

    private List<Integer> types = new ArrayList<>();
    private Integer checkedItem;

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
        GeoPoint point = findNearestPointInPolyline(currentLocation);

        if (polyline == null) {
            polyline = mapViews.setPolyline((point));
            polyline.setColor(Color.RED);
        } else {
            polyline.addPoint(point);
        }

        currentIndex = allGeoPoints.indexOf(point);

        Integer index = geoPoints.indexOf(point);
        detach(index);

        getCurrentTurn(toLocation(currentGeoPoint));
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
    public void getMarkers() {
        if (currentGeoPoint != null) {
            getCurrentTurn(toLocation(currentGeoPoint));
            //MarkersWithSettings(currentGeoPoint);
        }
    }

    private void getMarkersWithSettings(GeoPoint pGeoPoint) {
        cleanUpMap();
        if (types != null && types.size() > 0) {
            mapInteractor.getListPoi(
                    this, pGeoPoint.getLatitude(), pGeoPoint.getLongitude(), checkedItem, getType()
            );
        }
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        allGeoPoints = new ArrayList<>();
        geoPoints = new ArrayList<>();
        turnsList = new ArrayList<>();

        try {
            for (Line encodeLine : setData1()) {
                Integer idLine = encodeLine.getIdLine();

                mapViews.setPolyline(decodeL(encodeLine.getPolyline()), idLine);

                GeoPoint startPoint = decodeP(encodeLine.getStartPoint());
                GeoPoint endPoint = decodeP(encodeLine.getEndPoint());
                mapViews.setPoints(startPoint);
                mapViews.setPoints(endPoint);

                List<GeoPoint> lineList = decodeL(encodeLine.getPolyline());

                if (currentGeoPoint != null) {
                    lineList.remove(0);
                    turnsList.add(endPoint);
                } else {
                    currentIndex = 0;
                    currentGeoPoint = startPoint;
                    turnsList.add(endPoint);
                    turnsList.add(startPoint);
                }

                allGeoPoints.addAll(lineList);
            }

            for (int i = 0; i < 5; i++) {
                geoPoints.add(allGeoPoints.get(i));
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

    private void cleanUpMap() {
        mapViews.removeMarkers();
        mapViews.removePolylines();
    }

    private void getCurrentTurn(Location currentLocation) {
        GeoPoint shortestDistance = getShortestDistance(turnsList, currentLocation);

        if (isMoreDistance(RADIUS, currentLocation, shortestDistance)) {
            mapInteractor.getListPoi(
                    this,
                    shortestDistance.getLatitude(),
                    shortestDistance.getLongitude(),
                    checkedItem,
                    getType());
        } else {
            mapViews.removeMarkers();
        }
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

    public Location toLocation(GeoPoint position) {
        Location location = new Location("");
        location.setLatitude(position.getLatitude());
        location.setLongitude(position.getLongitude());
        return location;
    }

    public static GeoPoint toGeoPoint(Location location) {
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    private void detach(int newCenterIndex) {
        //int start = geoPoints.size();
        GeoPoint geoPoint = geoPoints.get(newCenterIndex);

        if (newCenterIndex > 2) {
            removeLast(geoPoint);
        }

        if (newCenterIndex < 2) {
            removePrevious(geoPoint);
        }
    }

    private void removeLast(GeoPoint geoPoint) {
        while (geoPoints.indexOf(geoPoint) - 1 >= 2) {
            geoPoints.remove(0);
        }

        //get next points
        if (geoPoints.size() == 3) {
            if (currentIndex + 1 < allGeoPoints.size()) {
                geoPoints.add(allGeoPoints.get(currentIndex + 1));
            } else {
                geoPoints.add(new GeoPoint((double) -geoPoints.size(), (double) -geoPoints.size()));
            }

        }

        if (geoPoints.size() == 4) {
            if (currentIndex + 2 < allGeoPoints.size()) {
                geoPoints.add(allGeoPoints.get(currentIndex + 2));
            } else {
                geoPoints.add(new GeoPoint((double) -geoPoints.size(), (double) -geoPoints.size()));
            }
        }
    }

    private void removePrevious(GeoPoint geoPoint) {
        while (geoPoints.indexOf(geoPoint) + 2 < geoPoints
                .indexOf(geoPoints.get(geoPoints.size() - 1))) {
            geoPoints.remove(geoPoints.size() - 1);
        }
        //get previous points
        List<GeoPoint> list = new ArrayList<>();
        if (geoPoints.size() == 3) {

            if (currentIndex > 1) {
                list.add(allGeoPoints.get(currentIndex - 1));
            } else {
                list.add(new GeoPoint((double) -geoPoints.size(), (double) -geoPoints.size()));
            }
            geoPoints.addAll(0, list);
            list.clear();
        }

        if (geoPoints.size() == 4) {
            if (currentIndex > 2) {
                list.add(allGeoPoints.get(currentIndex - 2));
            } else {
                list.add(new GeoPoint((double) -geoPoints.size() - 1,
                        (double) -geoPoints.size() - 1));
            }
            geoPoints.addAll(0, list);
        }
    }

    private GeoPoint findNearestPointInPolyline(Location currentLocation) {
        GeoPoint shortestDistance = getShortestDistance(geoPoints, currentLocation);

        if (geoPoints.indexOf(shortestDistance) != geoPoints.indexOf(currentGeoPoint)) {
            currentGeoPoint = shortestDistance;
        }
        return shortestDistance;
    }

    private GeoPoint getShortestDistance(List<GeoPoint> geoPointList, Location currentLocation) {
        Location endLocation = toLocation(geoPointList.get(0));
        Float distance = getDistanceBetween(currentLocation, endLocation);
        GeoPoint geoPoint = geoPointList.get(0);

        for (GeoPoint point : geoPointList) {
            if (isMoreDistance(distance, currentLocation, point)) {
                distance = getDistanceBetween(currentLocation, toLocation(point));
                geoPoint = point;
            }
        }

        Logs.e(TAG, distance + " meters");
        return geoPoint;
    }

    private boolean isMoreDistance(float distance, Location currentLocation, GeoPoint geoPoint) {
        return distance > getDistanceBetween(currentLocation, toLocation(geoPoint));
    }

    private float getDistanceBetween(Location startLocation, Location endLocation) {
        return startLocation.distanceTo(endLocation);
    }

    private static List<Line> setData1() {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(1, "ahtfI{iopC", "iitfIenopC", "ahtfI{iopCIa@G]G[EWGU"));
        lines.add(new Line(2, "iitfIenopC", "iotfIulopC", "iitfIenopCG@MBOBSBUDSBSDSBQBSDMB"));
        lines.add(new Line(3, "iotfIulopC", "wotfI_jopC", "iotfIulopC@NAh@MZ"));
        lines.add(new Line(4, "wotfI_jopC", "sdufIacopC",
                "wotfI_jopCQDQDQDOBQDQDQDQBODQDQDQBQDQDQDOBQDQDQDQDQDODQDQDQFQDODQDQDQDQDODQDQDQDQDQDMD"));
        /*lines.add(new Line(5,"","",""));
        lines.add(new Line(6,"","",""));
        lines.add(new Line(7,"","",""));*/
        return lines;
    }

}

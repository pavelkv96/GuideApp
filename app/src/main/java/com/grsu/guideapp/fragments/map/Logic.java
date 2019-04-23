package com.grsu.guideapp.fragments.map;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.base.listeners.OnChangePolyline;
import com.grsu.guideapp.base.listeners.OnNotFound;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MapUtils;
import java.util.ArrayList;
import java.util.List;

class Logic {

    private static final String TAG = Logic.class.getSimpleName();
    private static Logic logic = null;
    private Point currentPosition;
    private int currentIndex;
    private List<Point> fivePoint;
    private List<Point> turnPoint;
    private List<Point> allPoint;
    private static OnChangePolyline onChangePolyline;
    private static OnNotFound onNotFound;

    static Logic getInstance(MapPresenter mapPresenter) {
        if (logic == null) {
            logic = new Logic();
            onChangePolyline = mapPresenter;
            onNotFound = mapPresenter;
        }
        return logic;
    }

    void detachLogic() {
        logic = null;
        onChangePolyline = null;
        onNotFound = null;
        currentPosition = null;
    }

    void initialData(List<Line> encodePolylines) {
        allPoint = getDecodeLine(encodePolylines);
        turnPoint = getTurnsList(encodePolylines);
        fivePoint = getNearestPoint();

        currentPosition = new Point(fivePoint.get(2));
        currentIndex = 2;
    }

    Point findNearestPointInPolyline(Location currentLocation) {
        Point shortestDistance = getShortestDistance(fivePoint, currentLocation);
        onNotFound.onNotFound(null);

        Location endLocation = MapUtils.toLocation(shortestDistance.getPosition());
        if (MapUtils.getDistanceBetween(currentLocation, endLocation) > 25) {
            shortestDistance = getShortestDistance(allPoint, currentLocation);

            if (shortestDistance.getDistance() <= 25) {
                setChange(shortestDistance.getNumber());
                currentPosition = shortestDistance;
                fivePoint = getNewList(shortestDistance);
                for (Point latLng : fivePoint) {
                    Log.e(TAG, "findNearestPointInPolyline: " + latLng.getPosition());
                }
                Log.e(TAG, "findNearestPointInPolyline: -----------------------------------");

            } else {
                onNotFound.onNotFound(shortestDistance.getDistance());
            }
        } else {
            if (fivePoint.indexOf(shortestDistance) != fivePoint.indexOf(currentPosition)) {
                setChange(shortestDistance.getNumber());
                currentPosition = shortestDistance;
                fivePoint = getNewList(shortestDistance);
                for (Point latLng : fivePoint) {
                    Log.e(TAG, "findNearestPointInPolyline1: " + latLng.getPosition());
                }
                Log.e(TAG, "findNearestPointInPolyline1: -----------------------------------");

            }
        }
        return currentPosition;
    }

    ///
    private List<Point> getNewList(Point point) {
        int index = allPoint.indexOf(point);
        return allPoint.subList(index - 2, index + 3);
    }

    ///
    Point getShortestDistance(List<Point> latLngList, Location currentLocation) {
        Location endLocation = MapUtils.toLocation(latLngList.get(0).getPosition());
        float distance = MapUtils.getDistanceBetween(currentLocation, endLocation);
        Point startPoint = latLngList.get(0);

        for (Point point : latLngList) {
            if (MapUtils.isMoreDistance(distance, currentLocation, point.getPosition())) {
                distance = MapUtils.getDistanceBetween(currentLocation,
                        MapUtils.toLocation(point.getPosition()));
                startPoint = point;
                startPoint.setDistance(distance);
            }
        }

        return startPoint;
    }

    ///
    private List<Point> getDecodeLine(List<Line> lines) {
        boolean flag = true;

        List<Point> allPoint = new ArrayList<>();
        for (Line line : lines) {
            List<LatLng> polyline;

            if (flag) {
                polyline = CryptoUtils.decodeL(line.getPolyline());
                flag = false;
            } else {
                polyline = CryptoUtils.decodeL(line.getPolyline());
                polyline.remove(0);
            }

            for (LatLng latLng : polyline) {
                allPoint.add(new Point(line.getIdLine(), latLng, -1f));
            }
        }

        LatLng position = new LatLng(-181, -181);
        int index = allPoint.size() - 1;
        allPoint.add(0, new Point(1, position, -1f));
        allPoint.add(0, new Point(1, position, -1f));
        allPoint.add(new Point(index + 1, position, -1f));
        allPoint.add(new Point(index + 1, position, -1f));

        return allPoint;
    }

    ///
    private List<Point> getTurnsList(List<Line> lines) {
        turnPoint = new ArrayList<>(lines.size() + 1);
        for (Line decodeLine : lines) {
            if (turnPoint.isEmpty()) {
                turnPoint.add(new Point(1, CryptoUtils.decodeP(decodeLine.getStartPoint()), -1f));
            }
            Integer id = decodeLine.getIdLine();
            turnPoint.add(new Point(id, CryptoUtils.decodeP(decodeLine.getEndPoint()), -1f));
        }

        return turnPoint;
    }

    ///
    private List<Point> getNearestPoint() {
        List<Point> points = new ArrayList<>(5);
        points.addAll(allPoint.subList(0, 5));
        return points;
    }

    ///
    Point getCurrentPosition() {
        return currentPosition;
    }

    ///
    private void setChange(Integer shortestDistance) {
        if (currentPosition.getNumber() > shortestDistance) {
            onChangePolyline.onChange(currentPosition.getNumber(), shortestDistance);
        }
        if (currentPosition.getNumber() < shortestDistance) {
            onChangePolyline.onChange(currentPosition.getNumber(), shortestDistance);
        }
    }

    List<Point> getTurnsList() {
        return turnPoint;
    }

    List<Point> getList() {
        return fivePoint;
    }

    public int getCount() {
        return allPoint.size() - 4;
    }

    public float getProgress() {
        return currentIndex - 2;
    }
}

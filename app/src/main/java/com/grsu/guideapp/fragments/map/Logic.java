package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.base.listeners.OnChangePolyline;
import com.grsu.guideapp.base.listeners.OnNotFound;
import com.grsu.guideapp.models.DecodeLine;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Point;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MapUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Logic {

    private static Logic logic = null;
    private List<Point> latLngs;
    private Point currentPosition;
    private ArrayList<DecodeLine> decodeLines;
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
        latLngs = null;
        currentPosition = null;
        decodeLines = null;
    }

    void initialData(List<Line> encodePolylines) {
        latLngs = new ArrayList<>();
        decodeLines = (ArrayList<DecodeLine>) getDecodeLine(encodePolylines);

        latLngs.addAll(getNearestPoint(decodeLines));
        currentPosition = new Point(latLngs.get(2));
    }

    Point findNearestPointInPolyline(Location currentLocation) {
        Point shortestDistance = getShortestDistance(latLngs, currentLocation);
        onNotFound.onNotFound(null);

        Location endLocation = MapUtils.toLocation(shortestDistance.getPosition());
        if (MapUtils.getDistanceBetween(currentLocation, endLocation) > 25) {
            List<Point> shortestPointsEveryPolyline = new ArrayList<>();
            for (DecodeLine decodeLine : decodeLines) {
                List<Point> latLngList = DecodeLine.toPointList(decodeLine);
                Point shortest = getShortestDistance(latLngList, currentLocation);
                shortestPointsEveryPolyline.add(shortest);
            }

            shortestDistance = Point.getShortestPoint(shortestPointsEveryPolyline);

            if (shortestDistance.getDistance() <= 25) {
                setChange(shortestDistance.getNumber());
                currentPosition = shortestDistance;
                latLngs = getNewList(shortestDistance);
            } else {
                onNotFound.onNotFound(shortestDistance.getDistance());
            }
        } else {
            if (latLngs.indexOf(shortestDistance) != latLngs.indexOf(currentPosition)) {
                setChange(shortestDistance.getNumber());
                currentPosition = shortestDistance;
                latLngs = getNewList(shortestDistance);
            }
        }
        return currentPosition;
    }

    private List<Point> getNewList(Point point) {
        List<Point> list = new ArrayList<>();

        DecodeLine decodeLine = decodeLines.get(point.getNumber() - 1);
        for (LatLng latLng : decodeLine.getPolyline()) {
            list.add(new Point(decodeLine.getIdLine(), latLng, -1f));
        }

        if (decodeLines.size() > point.getNumber()) {
            decodeLine = decodeLines.get(point.getNumber());
            for (LatLng latLng : decodeLine.getPolyline()) {
                list.add(new Point(decodeLine.getIdLine(), latLng, -1f));
            }
        }

        if (list.get(0).getPosition() == point.getPosition() || list.get(1).getPosition() == point
                .getPosition()) {
            decodeLine = decodeLines.get(point.getNumber() - 2);
            List<LatLng> latLngList = new ArrayList<>(decodeLine.getPolyline());
            Collections.reverse(latLngList);
            for (LatLng latLng : latLngList) {
                list.add(0, new Point(decodeLine.getIdLine(), latLng, -1f));
            }
        }

        int index = list.indexOf(point);
        return list.subList(index - 2, index + 3);
    }

    Point getShortestDistance(List<Point> latLngList, Location currentLocation) {
        Location endLocation = MapUtils.toLocation(latLngList.get(0).getPosition());
        Float distance = MapUtils.getDistanceBetween(currentLocation, endLocation);
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

    private static List<DecodeLine> getDecodeLine(List<Line> lines) {
        List<DecodeLine> decodeLines = new ArrayList<>();
        for (Line line : lines) {
            decodeLines.add(new DecodeLine(
                    line.getIdLine(),
                    CryptoUtils.decodeP(line.getStartPoint()),
                    CryptoUtils.decodeP(line.getEndPoint()),
                    CryptoUtils.decodeL(line.getPolyline()),
                    line.getAudioReference())
            );
        }

        decodeLines.get(0).getPolyline().add(0, new LatLng(-181, -181));
        decodeLines.get(0).getPolyline().add(0, new LatLng(-181, -181));
        int index = decodeLines.size() - 1;
        decodeLines.get(index).getPolyline().add(new LatLng(-181, -181));
        decodeLines.get(index).getPolyline().add(new LatLng(-181, -181));

        return decodeLines;
    }

    List<Point> getTurnsList() {
        List<Point> latLngs = new ArrayList<>();

        for (DecodeLine decodeLine : decodeLines) {
            if (latLngs.isEmpty()) {
                latLngs.add(new Point(decodeLine.getIdLine(), decodeLine.getStartPoint(), -1f));
            }
            latLngs.add(new Point(decodeLine.getIdLine(), decodeLine.getEndPoint(), -1f));
        }

        return latLngs;
    }

    private static List<Point> getNearestPoint(List<DecodeLine> decodeLines) {
        List<Point> points = new ArrayList<>();
        DecodeLine decodeLine = decodeLines.get(0);
        for (LatLng latLng : decodeLine.getPolyline().subList(0, 5)) {
            points.add(new Point(decodeLine.getIdLine(), latLng, -1f));
        }

        return points;
    }

    Point getCurrentPosition() {
        return currentPosition;
    }

    /*List<DecodeLine> getDecodeLines() {
        ArrayList<DecodeLine> decodeLineList = new ArrayList<>();

        for (DecodeLine line : decodeLines) {
            decodeLineList.add(line.clone());
        }

        decodeLineList.get(0).getPolyline().remove(0);
        decodeLineList.get(0).getPolyline().remove(0);
        List<LatLng> polyline = decodeLineList.get(decodeLineList.size() - 1).getPolyline();
        polyline.remove(polyline.size() - 1);
        polyline.remove(polyline.size() - 1);
        return decodeLineList;
    }*/

    private void setChange(Integer shortestDistance) {
        if (currentPosition.getNumber() > shortestDistance) {
            onChangePolyline.onChange(currentPosition.getNumber(), shortestDistance);
        }
        if (currentPosition.getNumber() < shortestDistance) {
            onChangePolyline.onChange(currentPosition.getNumber(), shortestDistance);
        }
    }
}

package com.grsu.guideapp.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class Point {

    private Integer number;
    private LatLng position;
    private Float distance;

    public Point(Integer number, LatLng position, Float distance) {
        this.number = number;
        this.position = position;
        this.distance = distance;
    }

    public Point(Point point) {
        number = point.getNumber();
        position = point.getPosition();
        distance = point.getDistance();
    }

    public Integer getNumber() {
        return number;
    }

    public LatLng getPosition() {
        return position;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public static Point getShortestPoint(List<Point> points) throws NullPointerException {
        if (points != null && !points.isEmpty()) {
            Point start = points.get(0);
            for (Point currentPoint : points) {
                Float distance = currentPoint.getDistance();
                if (start.getDistance() > distance && distance > 0) {
                    start = currentPoint;
                }
            }
            return start;
        }

        throw new NullPointerException("List Points is empty");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;

        return number.equals(point.number) && position.equals(point.position);
    }
}

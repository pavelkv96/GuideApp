package com.grsu.guideapp.models;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DecodeLine implements Serializable, Cloneable {

    private Integer idLine;
    private LatLng startPoint;
    private LatLng endPoint;
    private List<LatLng> polyline;
    private String audioReference;
    private final static long serialVersionUID = 1137436432272859181L;

    public DecodeLine(Integer idLine, LatLng startPoint, LatLng endPoint, List<LatLng> polyline, String audioReference) {
        this.idLine = idLine;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.polyline = polyline;
        this.audioReference = audioReference;
    }

    public Integer getIdLine() {
        return idLine;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public List<LatLng> getPolyline() {
        return polyline;
    }

    public String getAudioReference() {
        return audioReference;
    }

    public static List<Point> toPointList(DecodeLine decodeLine) {
        List<Point> points = new ArrayList<>();
        for (LatLng latLng : decodeLine.getPolyline()) {
            points.add(new Point(decodeLine.getIdLine(), latLng, -1f));
        }

        return points;
    }

    @Override
    public DecodeLine clone() {
        try {
            DecodeLine line = (DecodeLine) super.clone();
            line.idLine = this.idLine;
            line.startPoint = new LatLng(this.startPoint.latitude, this.startPoint.longitude);
            line.endPoint = new LatLng(this.endPoint.latitude, this.endPoint.longitude);
            line.polyline = new ArrayList<>(this.polyline);
            line.audioReference = this.audioReference;
            return line;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
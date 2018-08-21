package com.grsu.guideapp.models;

import java.io.Serializable;
import org.osmdroid.views.overlay.Marker;

public class LineG implements Serializable {

    private Integer idLine;
    private Marker startPoint;
    private Marker endPoint;
    private String polyline;
    private final static long serialVersionUID = 1137436432272859181L;

    /**
     * No args constructor for use in serialization
     */
    public LineG() {
    }

    /**
     * @param polyline
     * @param endPoint
     * @param idLine
     * @param startPoint
     */
    public LineG(Integer idLine, Marker startPoint, Marker endPoint, String polyline) {
        super();
        this.idLine = idLine;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.polyline = polyline;
    }

    public Integer getIdLine() {
        return idLine;
    }

    public void setIdLine(Integer idLine) {
        this.idLine = idLine;
    }

    public Marker getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Marker startPoint) {
        this.startPoint = startPoint;
    }

    public Marker getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Marker endPoint) {
        this.endPoint = endPoint;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    @Override
    public String toString() {
        return "LINE " + "id=" + idLine + ", startPoint=" + startPoint.getPosition() +
                ", endPoint=" + endPoint.getPosition() + '}';
    }
}

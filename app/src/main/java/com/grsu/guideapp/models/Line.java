package com.grsu.guideapp.models;

import android.database.Cursor;
import java.io.Serializable;

public class Line implements Serializable {

    //POJO model

    private Integer idLine;
    private String startPoint;
    private String endPoint;
    private String polyline;
    private final static long serialVersionUID = 1137436432272859182L;

    /**
     * No args constructor for use in serialization
     */
    public Line() {
    }

    /**
     * @param polyline
     * @param endPoint
     * @param idLine
     * @param startPoint
     */
    public Line(Integer idLine, String startPoint, String endPoint, String polyline) {
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

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public static Line fromCursor(Cursor cur) {
        return new Line(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3));
    }
}

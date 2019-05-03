package com.grsu.guideapp.models;

import android.database.Cursor;
import java.io.Serializable;

public class Route implements Serializable {

    //POJO model
    private Integer idRoute;
    private String nameRoute;
    private Integer duration;
    private Integer distance;
    private String referencePhotoRoute;
    private String southwest;
    private String northeast;
    private Integer is_full;
    private final static long serialVersionUID = -4447199344994382402L;

    /**
     * No args constructor for use in serialization
     */
    public Route() {
    }

    /**
     * @param idRoute the identifier route
     * @param nameRoute the name route
     * @param duration the duration the route
     * @param distance the distance the route
     * @param referencePhotoRoute the link photo the route
     */

    public Route(Integer idRoute, String nameRoute, Integer duration, Integer distance,
            String referencePhotoRoute, String southwest, String northeast, int is_full) {
        this.idRoute = idRoute;
        this.nameRoute = nameRoute;
        this.duration = duration;
        this.distance = distance;
        this.referencePhotoRoute = referencePhotoRoute;
        this.southwest = southwest;
        this.northeast = northeast;
        this.is_full = is_full;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public String getNameRoute() {
        return nameRoute;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getReferencePhotoRoute() {
        return referencePhotoRoute;
    }

    public String getSouthwest() {
        return southwest;
    }

    public String getNortheast() {
        return northeast;
    }

    public void setIsFull(Integer is_full) {
        this.is_full = is_full;
    }

    public Integer getIsFull() {
        return is_full;
    }

    public static Route fromCursor(Cursor cur) {
        return new Route(cur.getInt(0), cur.getString(1), cur.getInt(2), cur.getInt(3),
                cur.getString(4), cur.getString(5), cur.getString(6), cur.getInt(7));
    }
}
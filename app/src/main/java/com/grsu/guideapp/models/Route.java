package com.grsu.guideapp.models;

import android.database.Cursor;
import java.io.Serializable;

public class Route implements Serializable {

    //POJO model
    private Integer idRoute;
    private String nameRoute;
    private Integer idAuthor;
    private Integer duration;
    private Integer distance;
    private String shortDescription;
    private String referencePhotoRoute;
    private String southwest;
    private String northeast;
    private final static long serialVersionUID = -4448199344994382402L;

    /**
     * No args constructor for use in serialization
     */
    public Route() {
    }

    /**
     * @param idRoute the identifier route
     * @param nameRoute the name route
     * @param idAuthor the identifier user(author)
     * @param duration the duration the route
     * @param distance the distance the route
     * @param shortDescription the short description the route
     * @param referencePhotoRoute the link photo the route
     */

    public Route(Integer idRoute, String nameRoute, Integer idAuthor, Integer duration,
            Integer distance, String shortDescription, String referencePhotoRoute, String southwest,
            String northeast) {
        this.idRoute = idRoute;
        this.nameRoute = nameRoute;
        this.idAuthor = idAuthor;
        this.duration = duration;
        this.distance = distance;
        this.shortDescription = shortDescription;
        this.referencePhotoRoute = referencePhotoRoute;
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public String getNameRoute() {
        return nameRoute;
    }

    public Integer getIdAuthor() {
        return idAuthor;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getShortDescription() {
        return shortDescription;
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

    public static Route fromCursor(Cursor cursor) {
        return new Route(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8));
    }
}
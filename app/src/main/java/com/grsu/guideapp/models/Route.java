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
            Integer distance, String shortDescription, String referencePhotoRoute) {
        this.idRoute = idRoute;
        this.nameRoute = nameRoute;
        this.idAuthor = idAuthor;
        this.duration = duration;
        this.distance = distance;
        this.shortDescription = shortDescription;
        this.referencePhotoRoute = referencePhotoRoute;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    public String getNameRoute() {
        return nameRoute;
    }

    public void setNameRoute(String nameRoute) {
        this.nameRoute = nameRoute;
    }

    public Integer getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(Integer idAuthor) {
        this.idAuthor = idAuthor;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getReferencePhotoRoute() {
        return referencePhotoRoute;
    }

    public void setReferencePhotoRoute(String referencePhotoRoute) {
        this.referencePhotoRoute = referencePhotoRoute;
    }

    public static Route fromCursor(Cursor cursor) {
        return new Route(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
    }
}
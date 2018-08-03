package com.grsu.guideapp.models;

import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNotNull.NotNull;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Route.class)
public class Route implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NotNull.NOT_NULL)
    public static final String ID_ROUTE = "id_route";

    @dbText
    public static final String NAME_ROUTE = "name_route";

    @dbInteger(isNotNull = NotNull.NOT_NULL)
    public static final String ID_AUTHOR = "id_author";

    @dbInteger
    public static final String DURATION = "duration";

    @dbInteger
    public static final String DISTANCE = "distance";

    @dbText
    public static final String SHORT_DESCRIPTION = "short_description";

    @dbText
    public static final String REFERENCE_PHOTO_ROUTE = "reference_photo_route";

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
}
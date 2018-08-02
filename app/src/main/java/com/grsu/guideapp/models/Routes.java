package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNotNull.NotNull;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Routes.class)
public class Routes implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NotNull.NOT_NULL)
    public static final String ID_ROUTE = "id_route";

    @dbText
    public static final String NAME_ROUTE = "name_route";

    @dbInteger(isNotNull = NotNull.NOT_NULL)
    public static final String TYPE = "type";

    @dbForeignKey(entity = Users.class, entityField = Users.UID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
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

    private Integer type;

    private Integer idAuthor;

    private Integer duration;

    private Integer distance;

    private String shortDescription;

    private String referencePhotoRoute;

    private final static long serialVersionUID = -4448199344994382402L;

    /**
     * No args constructor for use in serialization
     */
    public Routes() {
    }

    /**
     * @param idRoute the identifier route
     * @param nameRoute the name route
     * @param type the type route, there is a list of constant values
     * @param idAuthor the identifier user(author)
     * @param duration the duration the route
     * @param distance the distance the route
     * @param shortDescription the short description the route
     * @param referencePhotoRoute the link photo the route
     */

    public Routes(Integer idRoute, String nameRoute, Integer type, Integer idAuthor,
            Integer duration, Integer distance, String shortDescription,
            String referencePhotoRoute) {
        this.idRoute = idRoute;
        this.nameRoute = nameRoute;
        this.type = type;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
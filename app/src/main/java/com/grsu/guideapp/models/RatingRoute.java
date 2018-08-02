package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;
import static com.grsu.database_library.annotations.dbNumeric.Numeric.NUMERIC;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNumeric;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(RatingRoute.class)
public class RatingRoute implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_RATING = "id_rating";

    @dbForeignKey(entity = Routes.class, entityField = Routes.ID_ROUTE, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_ROUTE = "id_route";

    @dbForeignKey(entity = Users.class, entityField = Users.UID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_AUTHOR = "id_author";

    @dbInteger(isNotNull = NOT_NULL)
    public static final String DATE_ASSESSMENT = "date_assessment";

    @dbNumeric(value = NUMERIC, isNotNull = NOT_NULL)
    public static final String ASSESSMENT = "assessment";

    @dbText
    public static final String COMMENT = "comment";

    //POJO model

    private Integer idRating;
    private Integer idRoute;
    private Integer idAuthor;
    private Integer dateAssessment;
    private String assessment;
    private String comment;
    private final static long serialVersionUID = -999655704868700650L;

    /**
     * No args constructor for use in serialization
     */
    public RatingRoute() {
    }

    /**
     * @param idRoute
     * @param idAuthor
     * @param idRating
     * @param comment
     * @param dateAssessment
     * @param assessment
     */
    public RatingRoute(Integer idRating, Integer idRoute, Integer idAuthor, Integer dateAssessment,
            String assessment, String comment) {
        super();
        this.idRating = idRating;
        this.idRoute = idRoute;
        this.idAuthor = idAuthor;
        this.dateAssessment = dateAssessment;
        this.assessment = assessment;
        this.comment = comment;
    }

    public Integer getIdRating() {
        return idRating;
    }

    public void setIdRating(Integer idRating) {
        this.idRating = idRating;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    public Integer getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(Integer idAuthor) {
        this.idAuthor = idAuthor;
    }

    public Integer getDateAssessment() {
        return dateAssessment;
    }

    public void setDateAssessment(Integer dateAssessment) {
        this.dateAssessment = dateAssessment;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

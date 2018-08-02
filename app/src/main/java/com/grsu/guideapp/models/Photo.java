package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Photo.class)
public class Photo implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_REFERENCE = "id_reference";

    @dbForeignKey(entity = Points.class, entityField = Points.ID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_POINTS = "id_points";

    @dbText(isNotNull = NOT_NULL)
    public static final String PHOTO_REFERENCE = "photo_reference";

    //POJO model

    private Integer idReference;
    private Integer idPoints;
    private String photoReference;
    private final static long serialVersionUID = 1465570296311622433L;

    /**
     * No args constructor for use in serialization
     */
    public Photo() {
    }

    /**
     * @param idPoints
     * @param photoReference
     * @param idReference
     */
    public Photo(Integer idReference, Integer idPoints, String photoReference) {
        super();
        this.idReference = idReference;
        this.idPoints = idPoints;
        this.photoReference = photoReference;
    }

    public Integer getIdReference() {
        return idReference;
    }

    public void setIdReference(Integer idReference) {
        this.idReference = idReference;
    }

    public Integer getIdPoints() {
        return idPoints;
    }

    public void setIdPoints(Integer idPoints) {
        this.idPoints = idPoints;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}

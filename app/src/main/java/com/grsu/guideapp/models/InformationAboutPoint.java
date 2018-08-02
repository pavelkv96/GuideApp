package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;
import static com.grsu.database_library.annotations.dbPrimaryKey.Key.UNIQUE;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(InformationAboutPoint.class)
public class InformationAboutPoint implements Serializable {

    //CONSTANTS

    @dbPrimaryKey(key = UNIQUE)
    @dbForeignKey(entity = Points.class, entityField = Points.ID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_INFO = "id_info";

    @dbText(isNotNull = NOT_NULL)
    public static final String SHORT_DESCRIPTION_POINT = "short_description_point";

    @dbText(isNotNull = NOT_NULL)
    public static final String AUDIO_REFERENCE = "audio_reference";


    //POJO model

    private Integer idInfo;
    private String shortDescriptionPoint;
    private String audioReference;

    private final static long serialVersionUID = -6094752779573843057L;

    /**
     * No args constructor for use in serialization
     */
    public InformationAboutPoint() {
    }

    /**
     * @param audioReference
     * @param shortDescriptionPoint
     * @param idInfo
     */
    public InformationAboutPoint(Integer idInfo, String shortDescriptionPoint, String audioReference) {
        super();
        this.idInfo = idInfo;
        this.shortDescriptionPoint = shortDescriptionPoint;
        this.audioReference = audioReference;
    }

    public Integer getIdInfo() {
        return idInfo;
    }

    public void setIdInfo(Integer idInfo) {
        this.idInfo = idInfo;
    }

    public String getShortDescriptionPoint() {
        return shortDescriptionPoint;
    }

    public void setShortDescriptionPoint(String shortDescriptionPoint) {
        this.shortDescriptionPoint = shortDescriptionPoint;
    }

    public String getAudioReference() {
        return audioReference;
    }

    public void setAudioReference(String audioReference) {
        this.audioReference = audioReference;
    }

}

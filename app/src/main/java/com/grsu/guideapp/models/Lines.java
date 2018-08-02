package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_NO_ACTION;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_NO_ACTION;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Lines.class)
public class Lines implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_LINE = "ID_LINE";

    @dbForeignKey(entity = Place.class, entityField = Place.ID_POINT, onDelete = DELETE_NO_ACTION, onUpdate = UPDATE_NO_ACTION)
    @dbText(isNotNull = NOT_NULL)
    public static final String START_POINT = "start_point";

    @dbForeignKey(entity = Place.class, entityField = Place.ID_POINT, onDelete = DELETE_NO_ACTION, onUpdate = UPDATE_NO_ACTION)
    @dbText(isNotNull = NOT_NULL)
    public static final String END_POINT = "end_point";

    @dbText(isNotNull = NOT_NULL)
    public static final String POLYLINE = "polyline";

    //POJO model

    private Integer id;
    private Integer idLine;
    private String startPoint;
    private String endPoint;
    private String polyline;
    private final static long serialVersionUID = 1137436432272859182L;

    /**
     * No args constructor for use in serialization
     */
    public Lines() {
    }

    /**
     * @param id
     * @param polyline
     * @param endPoint
     * @param idLine
     * @param startPoint
     */
    public Lines(Integer id, Integer idLine, String startPoint, String endPoint, String polyline) {
        super();
        this.id = id;
        this.idLine = idLine;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.polyline = polyline;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}

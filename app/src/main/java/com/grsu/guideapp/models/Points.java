package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_NO_ACTION;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_NO_ACTION;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNumeric;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Points.class)
public class Points implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID = "id";

    @dbForeignKey(entity = Routes.class, entityField = Routes.ID_ROUTE, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_ROUTE = "id_route";

    @dbForeignKey(entity = Place.class, entityField = Place.ID_POINT, onDelete = DELETE_NO_ACTION, onUpdate = UPDATE_NO_ACTION)
    @dbText
    public static final String ID_POINT = "id_point";

    @dbNumeric(isNotNull = NOT_NULL)
    public static final String IS_POI = "is_poi";

    @dbText
    public static final String TEXT_INSTRUCTION = "text_instruction";

    //POJO model

    private Integer id;
    private Integer idRoute;
    private String idPoint;
    private String address;
    private Integer isPoi;
    private String textInstruction;
    private final static long serialVersionUID = 1743533885186767174L;

    /**
     * No args constructor for use in serialization
     */
    public Points() {
    }

    /**
     * @param id
     * @param idRoute
     * @param address
     * @param textInstruction
     * @param idPoint
     * @param isPoi
     */
    public Points(Integer id, Integer idRoute, String idPoint, String address, Integer isPoi, String textInstruction) {
        super();
        this.id = id;
        this.idRoute = idRoute;
        this.idPoint = idPoint;
        this.address = address;
        this.isPoi = isPoi;
        this.textInstruction = textInstruction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    public String getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(String idPoint) {
        this.idPoint = idPoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsPoi() {
        return isPoi;
    }

    public void setIsPoi(Integer isPoi) {
        this.isPoi = isPoi;
    }

    public String getTextInstruction() {
        return textInstruction;
    }

    public void setTextInstruction(String textInstruction) {
        this.textInstruction = textInstruction;
    }

}

package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_NO_ACTION;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_NO_ACTION;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;

@dbTable(Lines.class)
public class Lines {

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_LINE = "ID_LINE";

    @dbForeignKey(entity = Place.class,entityField = Place.ID_POINT,onDelete = DELETE_NO_ACTION,onUpdate = UPDATE_NO_ACTION)
    @dbText(isNotNull = NOT_NULL)
    public static final String START_POINT = "start_point";

    @dbForeignKey(entity = Place.class,entityField = Place.ID_POINT,onDelete = DELETE_NO_ACTION,onUpdate = UPDATE_NO_ACTION)
    @dbText(isNotNull = NOT_NULL)
    public static final String END_POINT = "end_point";

    @dbText(isNotNull = NOT_NULL)
    public static final String POLYLINE = "polyline";
}

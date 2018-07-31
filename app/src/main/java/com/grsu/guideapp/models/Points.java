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

@dbTable(Points.class)
public class Points {

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
}

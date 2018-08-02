package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_NO_ACTION;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_NO_ACTION;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import java.io.Serializable;

@dbTable(ListLines.class)
public class ListLines implements Serializable {

    //CONSTANTS

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID = "id";

    @dbForeignKey(entity = Lines.class, entityField = Lines.ID_LINE, onDelete = DELETE_NO_ACTION, onUpdate = UPDATE_NO_ACTION)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_LINE = "id_line";

    @dbForeignKey(entity = Routes.class, entityField = Routes.ID_ROUTE, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_ROUTE = "id_route";

    //POJO model

    //TODO create pojo model
}

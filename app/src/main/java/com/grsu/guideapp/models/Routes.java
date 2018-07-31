package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNotNull.NotNull;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;

@dbTable(Routes.class)
public class Routes {

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
}
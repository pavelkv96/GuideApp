package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;

@dbTable(Photo.class)
public class Photo {

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_REFERENCE = "id_reference";

    @dbForeignKey(entity = Points.class, entityField = Points.ID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_POINTS = "id_points";

    @dbText(isNotNull = NOT_NULL)
    public static final String PHOTO_REFERENCE = "photo_reference";

}

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

@dbTable(InformationAboutPoint.class)
public class InformationAboutPoint {

    @dbPrimaryKey(key = UNIQUE)
    @dbForeignKey(entity = Points.class, entityField = Points.ID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_INFO = "id_info";

    @dbText(isNotNull = NOT_NULL)
    public static final String SHORT_DESCRIPTION_POINT = "short_description_point";

    @dbText(isNotNull = NOT_NULL)
    public static final String AUDIO_REFERENCE = "audio_reference";
}

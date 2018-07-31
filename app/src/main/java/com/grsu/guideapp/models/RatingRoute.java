package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.DELETE_CASCADE;
import static com.grsu.database_library.annotations.dbForeignKey.ChangeData.UPDATE_CASCADE;
import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;
import static com.grsu.database_library.annotations.dbNumeric.Numeric.NUMERIC;

import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNumeric;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;

@dbTable(RatingRoute.class)
public class RatingRoute {

    @dbPrimaryKey
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_RATING = "id_rating";

    @dbForeignKey(entity = Routes.class,entityField = Routes.ID_ROUTE, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_ROUTE = "id_route";

    @dbForeignKey(entity = Users.class,entityField = Users.UID, onDelete = DELETE_CASCADE, onUpdate = UPDATE_CASCADE)
    @dbInteger(isNotNull = NOT_NULL)
    public static final String ID_AUTHOR = "id_author";

    @dbInteger(isNotNull = NOT_NULL)
    public static final String DATE_ASSESSMENT = "date_assessment";

    @dbNumeric(value = NUMERIC, isNotNull = NOT_NULL)
    public static final String ASSESSMENT = "assessment";

    @dbText
    public static final String COMMENT = "comment";
}

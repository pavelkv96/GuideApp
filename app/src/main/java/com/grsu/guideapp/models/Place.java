package com.grsu.guideapp.models;

import static com.grsu.database_library.annotations.dbNotNull.NotNull.NOT_NULL;
import static com.grsu.database_library.annotations.dbPrimaryKey.Key.UNIQUE;

import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNumeric;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Place.class)
public class Place implements Serializable{

    @dbPrimaryKey(key = UNIQUE)
    @dbText
    public static final String ID_POINT = "id_point";

    @dbText
    public static final String ADDRESS = "address";

    @dbNumeric(isNotNull = NOT_NULL)
    public static final String latitude = "latitude";

    @dbNumeric(isNotNull = NOT_NULL)
    public static final String longitude = "longitude";

    //TODO delete
    @dbText
    public static final String J = "J";

    //TODO delete
    @dbInteger
    public static final String l = "l";

    //POJO model
    //TODO create pojo model

}

package com.grsu.guideapp.models;

import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNotNull.NotNull;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;

@dbTable(Users.class)
public class Users {

    @dbPrimaryKey
    @dbInteger(isNotNull = NotNull.NOT_NULL)
    public static final String UID = "uid";

    @dbText(isNotNull = NotNull.NOT_NULL)
    public static final String LOGIN = "login";

    @dbText(isNotNull = NotNull.NOT_NULL)
    public static final String DISPLAY_NAME = "display_name";

    @dbText(isNotNull = NotNull.NOT_NULL)
    public static final String PASSWORD = "password";

    @dbInteger(isNotNull = NotNull.NOT_NULL)
    public static final String ROLE = "role";

    @dbText
    public static final String PHOTO_REFERENCE = "photo_reference";

    @dbText
    public static final String ACCESS_TOKEN = "access_token";

}

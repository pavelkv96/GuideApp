package com.grsu.guideapp.models;

import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNotNull.NotNull;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import java.io.Serializable;

@dbTable(Users.class)
public class Users implements Serializable {

    //CONSTANTS

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

    //POJO model

    private Integer uid;
    private String login;
    private String displayName;
    private String photoReference;
    private final static long serialVersionUID = -1661840585782184694L;

    /**
     * No args constructor for use in serialization
     */
    public Users() {
    }

    /**
     * @param uid
     * @param login
     * @param displayName
     * @param photoReference
     */
    public Users(Integer uid, String login, String displayName, String photoReference) {
        super();
        this.uid = uid;
        this.login = login;
        this.displayName = displayName;
        this.photoReference = photoReference;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

}

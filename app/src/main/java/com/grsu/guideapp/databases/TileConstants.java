package com.grsu.guideapp.databases;

public interface TileConstants {

    String TABLE = "tiles";
    String COLUMN_KEY = "key";
    String COLUMN_PROVIDER = "provider";
    String COLUMN_TILE = "tile";
    String COLUMN_EXPIRES = "expires";

    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" + COLUMN_KEY
            + " INTEGER , " + COLUMN_PROVIDER + " TEXT, " + COLUMN_TILE
            + " BLOB, "
            + COLUMN_EXPIRES + " INTEGER, PRIMARY KEY (" + COLUMN_KEY + ", "
            + COLUMN_PROVIDER + "));";
}

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

    String primaryKey = COLUMN_KEY + "=? and " + COLUMN_PROVIDER + "=? limit 1";

    String[] columns = {COLUMN_TILE};

    //SQLite Exceptions constant
    String SQLITE_BIND_OR_COLUMN_INDEX_OUT_OF_RANGE_EXCEPTION = "SQLiteBindOrColumnIndexOutOfRangeException";
    String SQLITE_BLOB_TOO_BIG_EXCEPTION = "SQLiteBlobTooBigException";
    String SQLITE_CONSTRAINT_EXCEPTION = "SQLiteConstraintException";
    String SQLITE_DATATYPE_MISMATCH_EXCEPTION = "SQLiteDatatypeMismatchException";
    String SQLITE_FULL_EXCEPTION = "SQLiteFullException";
    String SQLITE_MISUSE_EXCEPTION = "SQLiteMisuseException";
    String SQLITE_TABLE_LOCKED_EXCEPTION = "SQLiteTableLockedException";

    String SQLITE_ABORT_EXCEPTION = "SQLiteAbortException";
    String SQLITE_ACCESS_PERM_EXCEPTION = "SQLiteAccessPermException";
    String SQLITE_CANT_OPEN_DATABASE_EXCEPTION = "SQLiteCantOpenDatabaseException";
    String SQLITE_DATABASE_CORRUPT_EXCEPTION = "SQLiteDatabaseCorruptException";
    String SQLITE_DATABASE_LOCKED_EXCEPTION = "SQLiteDatabaseLockedException";
    String SQLITE_DISK_IOEXCEPTION = "SQLiteDiskIOException";
    String SQLITE_DONE_EXCEPTION = "SQLiteDoneException";
    String SQLITE_OUT_OF_MEMORY_EXCEPTION = "SQLiteOutOfMemoryException";
    String SQLITE_READ_ONLY_DATABASE_EXCEPTION = "SQLiteReadOnlyDatabaseException";
}

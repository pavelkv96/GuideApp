package com.grsu.guideapp.data.local.database.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grsu.guideapp.map.Provider
import java.util.*

@Entity(tableName = "tiles")
class CacheTile(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "key_tile", index = true) val key: Long,

    @ColumnInfo(name = "style", index = true) val style: Provider,

    @ColumnInfo(name = "tile") val tile: ByteArray,

    @ColumnInfo(name = "expires") val expires: Date
)
/*

@Entity(
    tableName = "tiles",
    indices = [Index("key_tile", "style", unique = true)],
    primaryKeys = ["key_tile", "style"]
)
class CacheTile(

    @ColumnInfo(name = "key_tile") val key: Long,

    @ColumnInfo(name = "style") val style: Provider,

    @ColumnInfo(name = "tile") val tile: ByteArray,

    @ColumnInfo(name = "expires") val expires: Date
)*/

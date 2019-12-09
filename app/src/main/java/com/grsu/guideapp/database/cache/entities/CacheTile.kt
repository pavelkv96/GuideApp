package com.grsu.guideapp.database.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grsu.guideapp.utils.Provider
import java.util.*

@Entity(tableName = "tiles")
class CacheTile(
    @PrimaryKey
    @ColumnInfo(name = "key_tile")
    var key: Long,

    //@PrimaryKey
    var style: Provider,

    var tile: ByteArray,

    var expires: Date
)
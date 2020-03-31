package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grsu.guideapp.utils.Status
import java.util.*

@Entity(tableName = "routes")
class Routes(
    @PrimaryKey
    @ColumnInfo(name = "id_route") val id_route: Long,

    @ColumnInfo(name = "duration", defaultValue = "0") val duration: Int,
    @ColumnInfo(name = "distance", defaultValue = "0") val distance: Int,
    @ColumnInfo(name = "photo_reference") val photo_reference: String?,
    @ColumnInfo(name = "southwest") val southwest: String,
    @ColumnInfo(name = "northeast") val northeast: String,
    @ColumnInfo(name = "last_update") val last_update: Date,
    @ColumnInfo(name = "last_download") val last_download: Date,
    @ColumnInfo(name = "status", defaultValue = "0") var status: Status
)
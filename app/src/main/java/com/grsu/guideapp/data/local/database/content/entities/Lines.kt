package com.grsu.guideapp.data.local.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lines")
class Lines(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_line")
    val id_line: Long,

    @ColumnInfo(name = "start_point") val start_point: String,
    @ColumnInfo(name = "end_point") val end_point: String,
    @ColumnInfo(name = "polyline") val polyline: String
)
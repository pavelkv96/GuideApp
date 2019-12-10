package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "marker",
    indices = [
        Index(value = ["link_icon"], unique = true)
    ]
)
class Marker(
    @PrimaryKey
    @ColumnInfo(name = "link_icon") val link: String,

    @ColumnInfo(name = "icon") val icon: ByteArray,
    @ColumnInfo(name = "last_update") val last_update: Long
)
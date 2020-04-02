package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

class RouteItemVO(
    @ColumnInfo(name = "id_route") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "photo_reference") val image: String,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "distance") val distance: Int
)
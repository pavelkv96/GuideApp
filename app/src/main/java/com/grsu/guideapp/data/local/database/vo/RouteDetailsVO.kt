package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

class RouteDetailsVO(
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "distance") val distance: Int,
    @ColumnInfo(name = "southwest") val southwest: String,
    @ColumnInfo(name = "northeast") val northeast: String,
    @ColumnInfo(name = "description") val description: String
)
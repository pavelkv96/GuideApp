package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

class LinesVO(
    @ColumnInfo(name = "sequence") val sequence: Int,
    @ColumnInfo(name = "start_point") val start: String,
    @ColumnInfo(name = "end_point") val end: String,
    @ColumnInfo(name = "polyline") val polyline: String
)
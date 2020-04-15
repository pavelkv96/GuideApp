package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

class PoiVO(
    @ColumnInfo(name = "id_poi") val id_poi: Int,
    @ColumnInfo(name = "id_type") val id_type: Int,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "link_icon") val link_icon: String
)
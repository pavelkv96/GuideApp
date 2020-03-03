package com.grsu.guideapp.database.vo

import androidx.room.ColumnInfo

class ObjectItemVO(
    @ColumnInfo(name = "id_poi") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "reference") val image: String?
)
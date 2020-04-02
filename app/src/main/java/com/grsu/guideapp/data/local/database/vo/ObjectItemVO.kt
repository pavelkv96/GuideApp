package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

class ObjectItemVO(
    @ColumnInfo(name = "id_poi") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "reference") val image: String?
)
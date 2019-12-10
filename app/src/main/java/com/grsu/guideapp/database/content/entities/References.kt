package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grsu.guideapp.utils.TypeResource

@Entity(tableName = "references")
class References(
    @PrimaryKey
    @ColumnInfo(name = "id_reference") val id_reference: String,

    @ColumnInfo(name = "reference") val reference: String,
    @ColumnInfo(name = "type_resource") val type_resource: TypeResource
)
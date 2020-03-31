package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.grsu.guideapp.utils.TypeResource

@Entity(tableName = "references_table")
class References(
    @PrimaryKey
    @ColumnInfo(name = "id_reference", index = true) val id_reference: String,

    @ColumnInfo(name = "reference") val reference: String,
    @ColumnInfo(name = "type_resource") val type_resource: TypeResource
)
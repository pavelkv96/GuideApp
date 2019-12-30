package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "references_list",
    primaryKeys = ["id_poi", "id_reference"],
    foreignKeys = [
        ForeignKey(
            entity = Poi::class,
            parentColumns = ["id_poi"],
            childColumns = ["id_poi"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = References::class,
            parentColumns = ["id_reference"],
            childColumns = ["id_reference"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
class ReferencesList(
    @ColumnInfo(name = "id_poi") val id_poi: Long,
    @ColumnInfo(name = "id_reference") val id_reference: String
)
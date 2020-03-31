package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "references_list",
    primaryKeys = ["id_poi", "id_reference"],
    indices = [Index("id_poi", "id_reference", unique = true)],
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
    @ColumnInfo(name = "id_reference", index = true) val id_reference: String
)
package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "list_poi",
    primaryKeys = ["id_route", "id_poi"],
    foreignKeys = [
        ForeignKey(
            entity = Routes::class,
            parentColumns = ["id_route"],
            childColumns = ["id_route"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Poi::class,
            parentColumns = ["id_poi"],
            childColumns = ["id_poi"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
class ListPoi(
    @ColumnInfo(name = "id_route", index = true) val id_route: Long,
    @ColumnInfo(name = "id_poi", index = true) val id_poi: Long,
    @ColumnInfo(name = "id_point") val id_point: String,
    @ColumnInfo(name = "distance") val distance: Double
)
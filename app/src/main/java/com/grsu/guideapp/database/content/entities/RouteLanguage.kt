package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "route_language",
    foreignKeys = [
        ForeignKey(
            entity = Routes::class,
            parentColumns = ["id_route"],
            childColumns = ["id_route"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class RouteLanguage(
    @PrimaryKey
    @ColumnInfo(name = "id_translate") val id_translate: Int,

    @ColumnInfo(name = "id_route") val id_route: Int,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String
)
package com.grsu.guideapp.data.local.database.content.entities

import androidx.room.*

@Entity(
    tableName = "route_language",
    indices = [Index("id_route", "language", unique = true)],
    primaryKeys = ["id_route", "language"],
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
    @ColumnInfo(name = "id_route") val id_route: Long,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "name", defaultValue = "") val name: String?,
    @ColumnInfo(name = "description", defaultValue = "") val description: String?
)
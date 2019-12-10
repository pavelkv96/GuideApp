package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "poi_language",
    foreignKeys = [
        ForeignKey(
            entity = Poi::class,
            parentColumns = ["id_poi"],
            childColumns = ["id_poi"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class PoiLanguage(
    @PrimaryKey
    @ColumnInfo(name = "id_translate") val id_translate: Int,

    @ColumnInfo(name = "id_poi") val id_poi: Int,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String
)
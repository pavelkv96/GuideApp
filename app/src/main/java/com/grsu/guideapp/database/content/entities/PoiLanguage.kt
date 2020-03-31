package com.grsu.guideapp.database.content.entities

import androidx.room.*

@Entity(
    tableName = "poi_language",
    indices = [Index("id_poi", "language", unique = true)],
    primaryKeys = ["id_poi", "language"],
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
    @ColumnInfo(name = "id_poi") val id_poi: Long,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "name", defaultValue = "") val name: String?,
    @ColumnInfo(name = "description", defaultValue = "") val description: String?
) {
    override fun toString() = "id_poi =$id_poi, language =$language, name =$name, description =$description"
}
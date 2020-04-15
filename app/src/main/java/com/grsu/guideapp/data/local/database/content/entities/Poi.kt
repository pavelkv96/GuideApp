package com.grsu.guideapp.data.local.database.content.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "poi",
    indices = [Index(value = ["id_poi"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Marker::class,
            parentColumns = ["link_icon"],
            childColumns = ["link_icon"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Types::class,
            parentColumns = ["id_type"],
            childColumns = ["id_type"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class Poi(
    @PrimaryKey
    @ColumnInfo(name = "id_poi") val id_poi: Long,

    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "link_icon", index = true) val link_icon: String,
    @ColumnInfo(name = "id_type", index = true) val id_type: Long,
    @ColumnInfo(name = "last_update") val last_update: Date,
    @ColumnInfo(name = "last_download") val last_download: Date,
    @ColumnInfo(name = "number") val number: Int?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "link") val link: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "is_favorite", defaultValue = "0") val is_favorite: Int
) {
    @Ignore
    constructor(
        id_poi: Long,
        location: String,
        link_icon: String,
        id_type: Long,
        last_update: Date,
        last_download: Date
    ) : this(
        id_poi,
        location,
        link_icon,
        id_type,
        last_update,
        last_download,
        null,
        null,
        null,
        null,
        null,
        0
    )
}
package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "list_lines",
    primaryKeys = ["id_line", "id_route"],
    foreignKeys = [
        ForeignKey(
            entity = Routes::class,
            parentColumns = ["id_route"],
            childColumns = ["id_route"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Lines::class,
            parentColumns = ["id_line"],
            childColumns = ["id_line"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
class ListLines(
    @ColumnInfo(name = "id_line", index = true) val id_line: Int,
    @ColumnInfo(name = "id_route", index = true) val id_route: Int,
    @ColumnInfo(name = "sequence") val sequence: Int
)
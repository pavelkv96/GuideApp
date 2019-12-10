package com.grsu.guideapp.database.content.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "types",
    indices = [Index(value = ["id_type"], unique = true)]
)
class Types(
    @PrimaryKey
    @ColumnInfo(name = "id_type") val id_type: Int,

    @ColumnInfo(name = "picture") val picture: ByteArray,
    @ColumnInfo(name = "language_ru") val language_ru: String,
    @ColumnInfo(name = "language_en") val language_en: String,
    @ColumnInfo(name = "language_cn") val language_cn: String,
    @ColumnInfo(name = "language_lt") val language_lt: String,
    @ColumnInfo(name = "language_pl") val language_pl: String,
    @ColumnInfo(name = "is_checked") val is_checked: Int
)
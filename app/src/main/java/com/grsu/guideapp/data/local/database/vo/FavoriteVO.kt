package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

interface FavoriteVO {
    val id: Int
    val name: String
    val photo: String
}

data class RouteFavoriteVO(
    @ColumnInfo(name = "id") override val id: Int,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "photo") override val photo: String
) : FavoriteVO

data class PoiFavoriteVO(
    @ColumnInfo(name = "id") override val id: Int,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "photo") override val photo: String
) : FavoriteVO
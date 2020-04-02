package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo

interface DetailsObjectVO {
    data class ObjectVO(
        @ColumnInfo(name = "location") val location: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "address") val address: String?,
        @ColumnInfo(name = "phone") val phone: String?,
        @ColumnInfo(name = "email") val email: String?,
        @ColumnInfo(name = "link") val link: String?
    )

    class ImagesVO(
        @ColumnInfo(name = "id_reference") val referenceId: String,
        @ColumnInfo(name = "reference") val reference: String?
    )
}
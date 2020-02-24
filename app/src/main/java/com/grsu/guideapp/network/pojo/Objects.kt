package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Objects(
    @SerializedName("id") val id: Long,
    @SerializedName("icon") val icon: String,
    @SerializedName("location") val location: Location,
    @SerializedName("categories") private val _categories: List<Long>?
) : Parcelable {
    val categories: List<Long> get() = _categories ?: listOf()
}
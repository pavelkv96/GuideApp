package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Root(
    @SerializedName("count") val count: Int,
    @SerializedName("part") val part: Int,
    @SerializedName("parts") val parts: Int,
    @SerializedName("in_parts") val in_parts: Int,
    @SerializedName("routes") private val _listRoutes: List<AbstractObject>?
) : Parcelable {
    val listRoutes: List<AbstractObject> get() = _listRoutes ?: listOf()
}
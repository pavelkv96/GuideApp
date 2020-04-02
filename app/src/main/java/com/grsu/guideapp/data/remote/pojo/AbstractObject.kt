package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AbstractObject(
    @SerializedName("id") val id: Long,
    @SerializedName("timestamp") private val _timestamp: Timestamp?,
    @SerializedName("active") val active: Boolean,
    @SerializedName("route") val routeObject: RouteObject?,
    @SerializedName("poi") val poiObject: PoiObject?
) : Parcelable {
    val timestamp: Timestamp get() = _timestamp ?: Timestamp(null, null)
}
package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Location(
    @SerializedName("lat") private val _lat: Double?,
    @SerializedName("lng") private val _lng: Double?
) : Parcelable {
    val latLng: LatLng get() = LatLng(_lat ?: -180.0, _lng ?: -90.0)
}
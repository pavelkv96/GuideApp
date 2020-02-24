package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Turn(
    @SerializedName("polyline") val polyline: String,
    @SerializedName("start") val start: Point,
    @SerializedName("end") val end: Point
) : Parcelable
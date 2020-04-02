package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Point(
    @SerializedName("location") val location: Location,
    @SerializedName("objects") val objects: List<Long>?
) : Parcelable
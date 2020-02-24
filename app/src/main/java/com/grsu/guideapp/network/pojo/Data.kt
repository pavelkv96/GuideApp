package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Data(
    @SerializedName("date") private val _date: Timestamp?,
    @SerializedName("lim_left") val limLeft: Location,
    @SerializedName("lim_right") val limRight: Location,
    @SerializedName("points") val points: List<Turn>,
    @SerializedName("objects") val objects: List<Objects>
) : Parcelable {
    val date: Timestamp get() = _date ?: Timestamp(null, null)
}
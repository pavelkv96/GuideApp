package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class RouteObject(
    @SerializedName("id") val id_route: Long,
    @SerializedName("content") val content: Content?,
    @SerializedName("data") val data: Data?
) : Parcelable

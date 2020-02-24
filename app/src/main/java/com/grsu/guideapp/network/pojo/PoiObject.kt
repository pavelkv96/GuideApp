package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class PoiObject(
    @SerializedName("content") val content: Content?,
    @SerializedName("property") val property: Property?
) : Parcelable
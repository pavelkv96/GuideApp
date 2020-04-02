package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Image(
    @SerializedName("href") val href: String,
    @SerializedName("isMain") val isMain: Boolean
) : Parcelable
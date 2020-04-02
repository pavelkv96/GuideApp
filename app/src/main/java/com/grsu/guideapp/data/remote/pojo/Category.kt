package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Category(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: Language
) : Parcelable
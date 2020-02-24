package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Property(
    @SerializedName("address") val address: Language?,
    @SerializedName("location") val location: Location,
    @SerializedName("icon") val icon: String,
    @SerializedName("phone") val phone: Phone,
    @SerializedName("email") val email: List<String>,
    @SerializedName("href") val href: List<String>
) : Parcelable
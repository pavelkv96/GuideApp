package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Language(
    @SerializedName("ru") private val _ru: String?,
    @SerializedName("en") private val _en: String?,
    @SerializedName("cn") private val _cn: String?,
    @SerializedName("pl") private val _pl: String?,
    @SerializedName("lt") private val _lt: String?
) : Parcelable {

    val ru: String get() = _ru ?: ""
    val en: String get() = _en ?: ""
    val cn: String get() = _cn ?: ""
    val pl: String get() = _pl ?: ""
    val lt: String get() = _lt ?: ""
}

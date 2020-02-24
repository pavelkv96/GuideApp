package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Phone(
    @SerializedName("mobile") private val _mobile: List<String>?,
    @SerializedName("office") private val _office: List<String>?,
    @SerializedName("fax") private val _fax: List<String>?
) : Parcelable {
    val mobile: List<String> get() = _mobile ?: listOf()
    val office: List<String> get() = _office ?: listOf()
    val fax: List<String> get() = _fax ?: listOf()
}
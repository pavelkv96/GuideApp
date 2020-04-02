package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Name(
    @SerializedName("full") private val _full: Language?
) : Parcelable {
    val full: Language get() = _full ?: Language(null, null, null, null, null)
}
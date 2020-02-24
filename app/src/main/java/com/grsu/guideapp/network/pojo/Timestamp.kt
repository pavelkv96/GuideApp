package com.grsu.guideapp.network.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Timestamp(
    @SerializedName("created_at") private val _createdAt: Date?,
    @SerializedName("updated_at") private val _updatedAt: Date?
) : Parcelable {
    val createdAt: Date get() = _createdAt ?: Date()
    val updatedAt: Date get() = _updatedAt ?: Date()
}
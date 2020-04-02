package com.grsu.guideapp.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Content(
    @SerializedName("name") val name: Name,
    @SerializedName("about") val about: Name,
    @SerializedName("images") private val _images: List<Image>?,
    @SerializedName("category") private val _categories: List<Category>?
) : Parcelable {
    val category: Category? get() = (_categories ?: listOf()).firstOrNull()
    val images: List<Image> get() = _images ?: listOf()
}
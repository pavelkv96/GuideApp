package com.grsu.guideapp.service

import android.location.Location
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocationState(var availability: Boolean, var location: Location?) : Parcelable {

    override fun toString(): String = "$availability and $location"
}
package com.grsu.guideapp.utils.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.grsu.guideapp.utils.MapUtils

fun LatLng.toLocation(): Location {
    return Location("").also {
        it.latitude = this.latitude
        it.longitude = this.longitude
    }
}

fun LatLng.distanceTo(to: LatLng): Double {
    return MapUtils.computeAngleBetween(this, to) * 6371009
}
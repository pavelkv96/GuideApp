package com.grsu.guideapp.utils

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat

object CheckPermission {

    private val groupStorage = arrayOf(
        permission.WRITE_EXTERNAL_STORAGE,
        permission.READ_EXTERNAL_STORAGE
    )

    private val groupLocation = arrayOf(
        permission.ACCESS_FINE_LOCATION
    )

    val allPermission = groupStorage + groupLocation

    fun checkStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, groupStorage[0]) == PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, groupStorage[1]) == PERMISSION_GRANTED
    }

    fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, groupLocation[0]) == PERMISSION_GRANTED
    }
}
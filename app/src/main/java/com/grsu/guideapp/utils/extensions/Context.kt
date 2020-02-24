package com.grsu.guideapp.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import com.grsu.guideapp.project_settings.Constants

fun Context.goToSettings(){
    val intent = Intent().apply {
        action = Constants.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts(Constants.PACKAGE, Constants.PACKAGE_NAME, null)
    }
    startActivity(intent)
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}
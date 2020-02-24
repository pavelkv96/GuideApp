package com.grsu.guideapp.utils.extensions

import com.grsu.guideapp.App
import com.grsu.guideapp.R


fun Int.toDuration(): String {
    return "5 ${App.getInstance().getString(R.string.short_hour)}"
}

fun Int.toDistance(): String {
    return "49.9 ${App.getInstance().getString(R.string.short_kilometers)}"
}
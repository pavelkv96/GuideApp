package com.grsu.guideapp.utils.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View

//fun Activity.findNavController(@IdRes viewId: Int): NavController = Navigation.findNavController(this, viewId)

fun Activity.isKeyboardOpen(): Boolean{
    val rootView = findViewById<View>(android.R.id.content)
    val visibleBounds = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = rootView.height - visibleBounds.height()
    val marginOfError = convertDpToPx(50F)

    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean {
    return isKeyboardOpen().not()
}
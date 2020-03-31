package com.grsu.guideapp.utils.extensions

import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.Navigation

//fun View.findNavController(): NavController = Navigation.findNavController(this)
fun View.navigate(@IdRes resId: Int) = Navigation.findNavController(this).navigate(resId)

fun View.isGone() = visibility == View.GONE

fun View.isVisible() = visibility == View.VISIBLE

fun View.isNotVisible() = visibility != View.VISIBLE

fun View.show() = run { if (isNotVisible()) visibility = View.VISIBLE }

fun View.hide() = run { if (isVisible()) visibility = View.GONE }
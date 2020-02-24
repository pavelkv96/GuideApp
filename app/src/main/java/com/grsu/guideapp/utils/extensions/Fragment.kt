package com.grsu.guideapp.utils.extensions

import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

//Navigation
fun Fragment.navigate(@IdRes resId: Int) = NavHostFragment.findNavController(this).navigate(resId)

fun Fragment.navigate(action: NavDirections) = NavHostFragment.findNavController(this).navigate(action)

fun Fragment.popBackStack() = NavHostFragment.findNavController(this).popBackStack()

//Permissions
/**
 * Fragment.requestPermissions()
 * @param permissions first parameter is code for response [Int] and second parameter is [Array] the requested permissions
 */
fun Fragment.requestPermissions(permissions: Pair<Int, Array<String>>) {
    return requestPermissions(permissions.second, permissions.first)
}

fun Fragment.isKeyboardOpen(): Boolean = requireActivity().isKeyboardOpen()

fun Fragment.isKeyboardClosed(): Boolean = requireActivity().isKeyboardClosed()

fun Fragment.getColor(@ColorRes color: Int) = ContextCompat.getColor(requireContext(), color)
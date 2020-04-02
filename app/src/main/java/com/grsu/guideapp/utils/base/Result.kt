package com.grsu.guideapp.utils.base

/*
old
sealed class Result<out T : Any> {
    class Success<out T : Any>(val data: T) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()
}
*/
sealed class Result<out T : Any> {
    class Success<out T : Any>(val data: T) : Result<T>()
    class Loading<out T : Any>(var isPreload: Boolean = false, var progress: Int = 0) : Result<T>()
    class Error(val error: String) : Result<Nothing>()
}
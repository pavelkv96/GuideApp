package com.grsu.guideapp.utils.base

import retrofit2.Response

@Suppress("UNUSED")
suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(response.errorBody()!!.toString())
    } catch (e: Exception) {
        return Result.Error(e.message ?: "Empty message")
    }
}
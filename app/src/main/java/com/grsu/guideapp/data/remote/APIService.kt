package com.grsu.guideapp.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("unused")
interface APIService {

    @GET("mobile/routes/category/101?Lang=all")
    suspend fun getListRoutes(): Response<com.grsu.guideapp.data.remote.pojo.Root>

    @GET("mobile/route/{id}/objects?Lang=all")
    suspend fun getPOIs(@Path("id") routeId: Long): Response<List<com.grsu.guideapp.data.remote.pojo.AbstractObject>>

    @GET("mobile/route/{id}?Lang=all")
    fun getPoi(@Path("id") id: Long): Call<Any>

    @GET("mobile/route/{id}?Lang=all")
    fun getRouteById(@Path("id") id: Long): Call<Any>

    @GET("external/search/objects?Lang=all&category=101")
    fun checkUpdateRoute(@Query("update_datetime") update_datetime: String): Call<Any>

    @GET("external/search/objects?Lang=all")
    fun checkUpdatePoi(@Query("update_datetime") update_datetime: String): Call<Any>

    @GET("mobile/route/{id}/objects?Lang=all")
    fun updatePoi(@Path("id") id: Long): Call<List<Any>>
}
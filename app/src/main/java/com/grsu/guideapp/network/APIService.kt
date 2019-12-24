package com.grsu.guideapp.network

import com.grsu.guideapp.network.model.Datum
import com.grsu.guideapp.network.model.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("unused")
interface APIService {

    @GET("mobile/routes/category/101?Lang=all")
    fun getRoutes(): Call<Root>

    @GET("mobile/route/{id}?Lang=all")
    fun getPoi(@Path("id") id: Long): Call<Datum>

    @GET("mobile/route/{id}?Lang=all")
    fun getRouteById(@Path("id") id: Long): Call<Datum>

    @GET("external/search/objects?Lang=all&category=101")
    fun checkUpdateRoute(@Query("update_datetime") update_datetime: String): Call<Root>

    @GET("external/search/objects?Lang=all")
    fun checkUpdatePoi(@Query("update_datetime") update_datetime: String): Call<Root>

    @GET("mobile/route/{id}/objects?Lang=all")
    fun updatePoi(@Path("id") id: Long): Call<List<Datum>>
}
package com.grsu.guideapp.network

import com.grsu.guideapp.network.interceptors.ApiInterceptor
import com.grsu.guideapp.project_settings.Settings
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    private val retrofit: Retrofit
    val api: APIService

    init {
        val client = with(OkHttpClient().newBuilder()) {
            addInterceptor(ApiInterceptor())
            build()
        }
        retrofit = with(Retrofit.Builder()) {
            baseUrl(Settings.BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
            build()
        }
        api = retrofit.create(APIService::class.java)
    }
}
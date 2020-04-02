package com.grsu.guideapp.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val newUrl = chain.request().url()
            .newBuilder()
            //TODO write key
            .addQueryParameter("apikey", "KEY")
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
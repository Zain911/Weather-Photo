package com.example.weatherphoto.data.network

import com.example.weatherphoto.BuildConfig
import okhttp3.Interceptor

class AuthInterceptor : Interceptor {


    private val queryAppIdText = "APPID"

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        val originalHttpUrl = originalRequest.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(queryAppIdText, BuildConfig.APPID)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }


}
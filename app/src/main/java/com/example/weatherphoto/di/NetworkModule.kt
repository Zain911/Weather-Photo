package com.example.weatherphoto.di

import com.example.weatherphoto.BuildConfig
import com.example.weatherphoto.data.network.AuthInterceptor
import com.example.weatherphoto.data.network.NetworkServices
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideWeatherServices(): NetworkServices {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor()).build()
            )
            .baseUrl(BuildConfig.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NetworkServices::class.java)
    }
}
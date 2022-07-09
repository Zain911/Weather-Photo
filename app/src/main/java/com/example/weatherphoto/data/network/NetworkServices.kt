package com.example.weatherphoto.data.network

import com.example.weatherphoto.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkServices {

    @GET("onecall?")
    suspend fun getWeatherStatsForLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String = "metric",
    ): Response<WeatherResponse>


}
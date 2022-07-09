package com.example.weatherphoto.ui.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherphoto.data.model.ImageItem
import com.example.weatherphoto.data.model.WeatherResponse
import com.example.weatherphoto.data.network.NetworkServices
import com.example.weatherphoto.data.repositories.ImagesRepository
import com.example.weatherphoto.domain.usecase.LocationLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val imageRepository: ImagesRepository,
    private val networkServices: NetworkServices
) : ViewModel() {

    val weatherResponse = MutableLiveData<WeatherResponse>()

    fun saveImageWithWeatherOverlay(bitmap: Bitmap, imagePath: String) {
        try {
            FileOutputStream(imagePath).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun getLocationWeatherStatus(
        lat: Double,
        lng: Double,
        imagePath: String,
        cityName: String
    ) {
        val response = networkServices.getWeatherStatsForLocation(lat, lng)
        if (response.isSuccessful) {
            weatherResponse.value = response.body()
            val body = response.body()

            imageRepository.addImage(
                ImageItem(
                    lat = body?.lat.toString(),
                    lng = body?.lat.toString(),
                    temperature = body?.current?.temp.toString(),
                    name = cityName,
                    date = LocalDateTime.now().toString(),
                    weatherCondition = body?.current?.weather?.get(0)?.description,
                    imageUrl = imagePath
                )
            )
        }
    }

    fun deleteImage(imagePath: String) = imageRepository.deleteImage(imagePath)

    private val locationData = LocationLiveData(context)

    fun getLocationData() = locationData


}
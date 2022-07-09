package com.example.weatherphoto.data.repositories

import androidx.lifecycle.LiveData
import com.example.weatherphoto.data.model.ImageItem

interface ImagesRepositoryInterface {

    suspend fun getSavedImages(): LiveData<List<ImageItem>>

    fun addImage(imageItem: ImageItem)

    fun deleteImage(imagePath : String)

}
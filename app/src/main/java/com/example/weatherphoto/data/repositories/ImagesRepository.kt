package com.example.weatherphoto.data.repositories

import androidx.lifecycle.LiveData
import com.example.weatherphoto.data.model.ImageItem
import com.example.weatherphoto.data.presistentstorage.room.ImagesDao
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val imagesDao: ImagesDao
) : ImagesRepositoryInterface {

    override suspend fun getSavedImages(): LiveData<List<ImageItem>> =
        imagesDao.getSavedImages()

    override fun addImage(imageItem: ImageItem) {
        imagesDao.addImage(imageItem)
    }

    override fun deleteImage(imagePath : String){
        imagesDao.deleteImage(imagePath)
    }

}
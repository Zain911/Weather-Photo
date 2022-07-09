package com.example.weatherphoto.data.presistentstorage.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.weatherphoto.data.model.ImageItem

@Dao
interface ImagesDao {

    @Insert(onConflict = REPLACE)
    fun addImage(imageItem: ImageItem)

    @Query("Select * From ImageItem")
    fun getSavedImages(): LiveData<List<ImageItem>>

    @Delete
    fun deleteImage(imageItem: ImageItem)

    @Query("Delete from ImageItem where image_url = :imagePath")
    fun deleteImage(imagePath: String)

    @Update
    fun updateImage(imageItem: ImageItem)

    @Query("SELECT COUNT(image_url) FROM ImageItem")
    fun getImagesCount(): LiveData<Int>

    @Query("DELETE FROM ImageItem")
    fun deleteAllImages()
}
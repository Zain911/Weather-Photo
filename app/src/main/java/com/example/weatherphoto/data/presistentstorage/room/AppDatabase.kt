package com.example.weatherphoto.data.presistentstorage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherphoto.data.model.ImageItem

@Database(entities = [ImageItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao
}
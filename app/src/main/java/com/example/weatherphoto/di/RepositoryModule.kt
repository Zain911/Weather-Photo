package com.example.weatherphoto.di

import com.example.weatherphoto.data.presistentstorage.room.ImagesDao
import com.example.weatherphoto.data.repositories.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideImagesRepository(
        imagesDao: ImagesDao,
    ): ImagesRepository {
        return ImagesRepository(imagesDao)
    }

}
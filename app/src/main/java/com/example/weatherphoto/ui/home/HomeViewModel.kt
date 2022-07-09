package com.example.weatherphoto.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherphoto.data.model.ImageItem
import com.example.weatherphoto.data.repositories.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val imagesRepository: ImagesRepository) :
    ViewModel() {

    lateinit var imagesList: LiveData<List<ImageItem>>

    init {
        viewModelScope.launch {
            imagesList = imagesRepository.getSavedImages()
        }
    }


}
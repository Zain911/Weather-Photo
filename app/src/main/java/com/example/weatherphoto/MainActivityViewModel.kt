package com.example.weatherphoto

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherphoto.domain.util.InternetConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(@ApplicationContext context: Context) :
    ViewModel() {
    val connectionLiveData = InternetConnectivity(context)
}
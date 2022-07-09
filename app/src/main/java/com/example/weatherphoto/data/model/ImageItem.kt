package com.example.weatherphoto.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageItem(

    @ColumnInfo(name = "temperature")
    var temperature: String? = null,

    @ColumnInfo(name = "weather_condition")
    var weatherCondition: String? = null,

    @ColumnInfo(name = "lat")
    var lat: String? = null,

    @ColumnInfo(name = "lng")
    var lng: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "image_date")
    var date: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "image_url")
    var imageUrl: String

)
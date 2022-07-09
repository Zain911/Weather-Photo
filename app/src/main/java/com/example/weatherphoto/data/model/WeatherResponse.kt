package com.example.weatherphoto.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "WeatherData")
data class WeatherResponse(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") var timezone: String,
    @Embedded
    @SerializedName("current") val current: Current,
    @SerializedName("daily") val daily: List<Daily>?,

) : Serializable {
    override fun toString(): String {
        return "WeatherResponse(lat=$lat, lon=$lon, timezone='$timezone', current=$current)"
    }
}

data class Current(
    @SerializedName("temp") val temp: Double,
    @SerializedName("weather") val weather: List<Weather>
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Daily(
    @SerializedName("temp") val temp: Temp,
    @SerializedName("weather") val weather: List<Weather>,

)

data class Temp(
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double,

)



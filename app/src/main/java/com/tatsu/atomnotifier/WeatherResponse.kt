package com.tatsu.atomnotifier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val current: CurrentWeather
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m")
    val temperature2m: Double,
    @SerialName("weather_code")
    val weatherCode: Int
)
package com.tatsu.atomnotifier

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class WeatherRepository {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json{
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun fetchWeather(): WeatherResponse {
        return client.get(
            "https://api.open-meteo.com/v1/forecast?latitude=36.6953&longitude=137.2113&current=temperature_2m,weather_code"
        ).body()
    }
}
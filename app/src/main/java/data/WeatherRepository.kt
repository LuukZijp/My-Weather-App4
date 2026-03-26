package com.example.myapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private val api = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    suspend fun getWeather(city: String): WeatherResponse {
        return api.getWeather(city, "cd77f2e3d195cb413b42afa08530931b")
    }
}
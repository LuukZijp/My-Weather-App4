package com.example.myapp.data

data class WeatherResponse(
    val name: String,
    val weather: List<WeatherCondition> = emptyList(),
    val main: Main,
    val wind: Wind
)

data class WeatherCondition(
    val main: String,
    val description: String
)

data class Main(
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

data class Wind(
    val speed: Double
)

package com.example.myapp.data

import retrofit2.http.GET

interface KnmiApi {
    @GET("abcws/event/query?format=rss&limit=30")
    suspend fun getEarthquakesRss(): String
}

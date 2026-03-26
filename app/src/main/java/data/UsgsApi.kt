package com.example.myapp.data

import retrofit2.http.GET
import retrofit2.http.Headers

interface UsgsApi {
    @Headers("Accept: application/atom+xml, text/xml")
    @GET("significant_month.atom")
    suspend fun getSignificantMonth(): String
}

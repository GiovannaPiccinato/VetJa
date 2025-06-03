package com.example.vetJa.API

import com.example.vetJa.API.GeoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeoClient {
    val api: GeoApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoApiService::class.java)
    }
}

package com.example.vetJa.API

import com.example.vetJa.models.GeoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApiService {
    @GET("search")
    fun getCoordinates(
        @Query("q") cep: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 1
    ): Call<List<GeoResponse>>
}

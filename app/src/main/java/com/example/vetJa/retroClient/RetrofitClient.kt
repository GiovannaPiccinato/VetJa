package com.example.vetJa.retroClient

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.vetJa.API.ApiService
import com.example.vetJa.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(private val context: Context) {
  
    private val _baseUrl = "http://10.135.111.20:8000"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(_baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)


}
package com.example.login

import API.ApiService
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.AddDialogFragment.OnUserCreatedListener
import com.example.login.models.user.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class VetListActivity : AppCompatActivity(), AddDialogFragment.OnUserCreatedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter

    private fun carregarUsuarios() {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Configuração do Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.15.17:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getAllUsers().enqueue(object : Callback<List<User>>  {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    val listener: OnUserCreatedListener = this@VetListActivity;
                    adapter = CustomAdapter(users, supportFragmentManager, this@VetListActivity)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API Error", "Response not successful. Code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("API Failure", "Error fetching products", t)
            }
        })
    }

    override fun onUserCreated() {
        carregarUsuarios()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        val floatBtn: FloatingActionButton = findViewById(R.id.float_btn);

        floatBtn.setOnClickListener {
            val dialog = AddDialogFragment();
            dialog.show(supportFragmentManager, dialog.tag);
        }

        recyclerView = findViewById(R.id.recyclerViewVet)
        recyclerView.layoutManager = LinearLayoutManager(this)

        carregarUsuarios();
    }
}

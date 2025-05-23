package com.example.vetJa.interceptor

import android.content.Context
import android.content.Intent
import com.example.vetJa.activitys.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = context.getSharedPreferences("user_token", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("user_token", null)

        val newRequest = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(newRequest)
    }
    private fun redirectToLogin() {
        // Limpa token salvo
        val sharedPref = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Abre a LoginActivity
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
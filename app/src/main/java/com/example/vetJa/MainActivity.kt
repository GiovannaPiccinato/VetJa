package com.example.vetJa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vetJa.activitys.IndexActivity
import com.example.vetJa.activitys.LoginActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val sharedPreferences = getSharedPreferences("user_token", MODE_PRIVATE)
//        val token = sharedPreferences.getString("user_token", "teste")
//
//        if (!token.isNullOrEmpty()) {
//            startActivity(Intent(this, IndexActivity::class.java))
//        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        //}

        finish()
    }

}
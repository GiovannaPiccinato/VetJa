package com.example.vetJa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vetJa.activitys.IndexActivity
import com.example.vetJa.activitys.IntroducaoActivity
import com.example.vetJa.activitys.LoginActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("user_token", MODE_PRIVATE)

        if (sharedPreferences.contains("user_token")) {
            startActivity(Intent(this, IndexActivity::class.java))
            finish()
        } else {
            val intent = Intent(this, IntroducaoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
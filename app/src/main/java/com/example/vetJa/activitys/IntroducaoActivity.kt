package com.example.vetJa.activitys

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetJa.R
import com.example.vetJa.databinding.ActivityIntroducaoBinding

class IntroducaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroducaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroducaoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonCadastro.setOnClickListener {
            startActivity(Intent(this, CadastrosActivity::class.java))
        }

        binding.textLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
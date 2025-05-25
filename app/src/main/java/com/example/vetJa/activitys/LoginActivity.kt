package com.example.vetJa.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.vetJa.R
import com.example.vetJa.models.login.LoginResponse
import retrofit2.*
import com.example.vetJa.models.login.LoginRequest
import com.example.vetJa.retroClient.RetrofitClient
import com.example.vetJa.utils.toast

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private val client = RetrofitClient(this).api;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)



        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            makeLogin(email, password)
        }
    }

    private fun makeLogin(email: String, password: String) {
        val request = LoginRequest(email, password)
        val call = client.login(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        val loginResponse = response.body()!!

                        Log.d("Body", loginResponse.toString())
                        Log.d("token", loginResponse.token.toString())

                        val sharedPreferences = getSharedPreferences("user_token", Context.MODE_PRIVATE)
                        sharedPreferences.edit() {
                            putString("user_token", loginResponse.token)
                        }

                        Log.d("token-shared", sharedPreferences.getString("user_token", "").toString())

                        toast("Olá, ${loginResponse.user!!.nome}, Seja bem-vindo(a)!", this@LoginActivity)

                        startActivity(Intent(this@LoginActivity, IndexActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Erro no login", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}

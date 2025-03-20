package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.models.login.LoginResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.example.login.models.login.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST


class LoginActivity : AppCompatActivity() {
    private val user = "teste"
    private val pass = "teste"
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            blockLogin()
        }
    }

    private fun blockLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.135.111.37:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val req = LoginRequest(email, password)
        val call = apiService.login(req)
        call.enqueue(object : Callback<List<LoginResponse>> {
            override fun onResponse(
                call: Call<List<LoginResponse>>,
                response: Response<List<LoginResponse>>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val loginResponses = response.body()!!
                    if (loginResponses.isNotEmpty() && loginResponses.last().status) {
                        loginResponses.last().token
                        val intent = Intent(this@LoginActivity, VetListActivity::class.java)
                        intent.putExtra("token", loginResponses.last().token)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Erro no login", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    interface ApiService {
        @POST("/auth/signIn")
        fun login(
            @Body req: LoginRequest
        ): Call<List<LoginResponse>>
    }
}

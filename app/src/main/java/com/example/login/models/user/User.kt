package com.example.login.models.user

data class User(
    val id: Int,
    val usuario: String,
    val email: String,
    val cpf: String,
    val senha: String,
)
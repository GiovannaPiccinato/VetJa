package com.example.vetJa.models.user

data class User(
    val id: String,
    val usuario: String,
    val nome: String,
    val email: String,
    val senha: String,
    val idCliente: String
)
package com.example.login.models.login


data class LoginResponse(
    val token: String?,
    val status: Boolean,
    val message: String
)
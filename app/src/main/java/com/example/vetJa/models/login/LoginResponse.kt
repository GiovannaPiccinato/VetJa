package com.example.vetJa.models.login

import com.example.vetJa.models.user.UserDTO


data class LoginResponse(
    val token: String,
    val user: UserDTO,
    val msg : String?
)
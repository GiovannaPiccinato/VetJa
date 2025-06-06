package com.example.vetJa.models.user
data class UserDTO(
    val nome: String?,
    val email: String?,
    val cpf: String?,
    val telefone: String?,
    val senha: String?,
//    val cep: String?,
//    val endereco: String?,
    var idCliente: String?
)

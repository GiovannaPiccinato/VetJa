package com.example.vetJa.models.Pet

data class PetDTO(
    val id: Int,
    val nome: String?,
    val gato: Boolean?,
    val idade: Int?,
    val macho: Boolean?
)
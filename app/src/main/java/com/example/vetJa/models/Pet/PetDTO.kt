package com.example.vetJa.models.Pet

//data class PetDTO(
//    val nome: String?,
//    val gato: Boolean?,
//    val idade: Int?,
//    val macho: Boolean?
//)

data class PetDTO(
    var idCliente: String?,
    val idAnimal: String?,
    val nome: String,
    val idade: String,
    val raca: String
//    val sexo: String,
//    val isCat: Boolean,
)
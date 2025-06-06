package com.example.vetJa.models.Pet

data class PetDTO(
    var idCliente: String?,
    var idAnimal: String?,
    var nome: String?,
    var gato: Boolean?, // Indica se é gato ou cachorro
    var idade: Int?, // Alterado para Int
    var macho: Boolean? // Indica o sexo do pet
)

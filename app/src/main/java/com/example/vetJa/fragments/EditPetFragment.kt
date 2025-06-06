package com.example.vetJa.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vetJa.R
import com.example.vetJa.databinding.FragmentEditPetBinding
import com.example.vetJa.models.Pet.PetDTO
import com.example.vetJa.retroClient.RetrofitClient

class EditPetFragment : Fragment() {

    lateinit var binding: FragmentEditPetBinding
    private lateinit var pet: PetDTO
    private var especie: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPetBinding.inflate(inflater, container, false)
        val petId = arguments?.getString("petId")
        if (petId != null) {
            getPetById(petId)
        } else {
            Log.e("EditPetFragment", "petId não encontrado nos argumentos")
        }
        binding.imageEspecieGato.setOnClickListener {
            switchBorderSpecie(true)
            especie = true
            binding.imageEspecieGato.isClickable = false
            binding.imageEspecieCachorro.isClickable = true
            Log.d("EditPetFragment", "Espécie: Gato")
        }

        binding.imageEspecieCachorro.setOnClickListener {
            switchBorderSpecie(false)
            especie = false
            binding.imageEspecieCachorro.isClickable = false
            binding.imageEspecieGato.isClickable = true
            Log.d("EditPetFragment", "Espécie: Cachorro")
        }

        binding.buttonAvancarCadastroPet.setOnClickListener {
            updateNewPet()
        }

        return binding.root
    }

    private fun getPetById(petId: String) {
        val retrofitClient = RetrofitClient(requireContext().applicationContext).api

        retrofitClient.getPetById(petId).enqueue(object : retrofit2.Callback<PetDTO> {
            override fun onResponse(
                call: retrofit2.Call<PetDTO>,
                response: retrofit2.Response<PetDTO>
            ) {
                if (response.isSuccessful) {
                    val petResponse = response.body()
                    if (petResponse != null) {
                        pet = petResponse // Inicializa a propriedade corretamente
                        Log.d("EditPetFragment", "Pet encontrado: $petResponse")
                        binding.editNomeCadastroPet.setText(petResponse.nome)
                        binding.editIdadePet.setText(petResponse.idade.toString())
                        binding.spinnerSexoPet.setSelection(
                            if (petResponse.macho == true) 1 else 2
                        )
                        switchBorderSpecie(petResponse.gato != false)
                    } else {
                        Log.e("EditPetFragment", "Pet não encontrado na resposta.")
                    }
                } else {
                    Log.e("EditPetFragment", "Erro na resposta: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<PetDTO>, t: Throwable) {
                Log.e("EditPetFragment", "Falha na conexão: ${t.message}")
            }
        })
    }

    private fun updateNewPet() {
        val nome = binding.editNomeCadastroPet.text.toString()
        val idade = binding.editIdadePet.text.toString().toIntOrNull()
        val sexo = defineSex(binding.spinnerSexoPet.selectedItem.toString())

        if (nome.isEmpty() || idade == null || sexo == "n") {
            Log.e("EditPetFragment", "Preencha todos os campos corretamente.")
            return
        }

        pet.idAnimal = pet.idAnimal ?: "0"
        pet.nome = nome
        pet.idade = idade
        pet.macho = sexo == "m"
        pet.gato = especie

        Log.d("EditPetFragment", "Atualizando pet: $pet")

        val retrofitClient = RetrofitClient(requireContext().applicationContext).api
        retrofitClient.updatePet(pet).enqueue(object : retrofit2.Callback<PetDTO> {
            override fun onResponse(call: retrofit2.Call<PetDTO>, response: retrofit2.Response<PetDTO>) {
                if (response.isSuccessful) {
                    Log.d("EditPetFragment", "Pet atualizado com sucesso.")
                } else {
                    Log.e("EditPetFragment", "Erro ao atualizar o pet: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<PetDTO>, t: Throwable) {
                Log.e("EditPetFragment", "Falha na conexão: ${t.message}")
            }
        })
    }

    private fun defineSex(sexo: String): String {
        return when (sexo) {
            "Macho" -> "m"
            "Fêmea" -> "f"
            else -> "n"
        }
    }

    fun switchBorderSpecie(isCat: Boolean) {
        if (isCat) {
            binding.imageEspecieGato.setBackgroundResource(R.drawable.border_selected)
            binding.imageEspecieCachorro.setBackgroundResource(R.drawable.border_default)
        } else {
            binding.imageEspecieCachorro.setBackgroundResource(R.drawable.border_selected)
            binding.imageEspecieGato.setBackgroundResource(R.drawable.border_default)
        }
    }
}
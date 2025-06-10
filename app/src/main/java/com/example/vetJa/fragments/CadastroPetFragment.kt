package com.example.vetJa.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vetJa.R
import com.example.vetJa.adapters.PetAdapter
import com.example.vetJa.databinding.FragmentCadastroPetBinding
import com.example.vetJa.models.Pet.PetDTO
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.retroClient.RetrofitClient
import com.example.vetJa.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroPetFragment : Fragment() {

    private lateinit var binding: FragmentCadastroPetBinding
    private lateinit var retrofitClient: RetrofitClient
    private var especie: Boolean = false
    var usuario: UserDTO? = null
    private lateinit var adapter: PetAdapter
    private val pet = mutableListOf<PetDTO>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCadastroPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofitClient = RetrofitClient(requireContext().applicationContext)

        binding.imageEspecieGato.setOnClickListener {
            switchBorderSpecie(true)
            especie = true
            binding.imageEspecieGato.isClickable = false
            binding.imageEspecieCachorro.isClickable = true
            Log.d("CadastroPetFragment", "Espécie: Gato")
        }

        binding.imageEspecieCachorro.setOnClickListener {
            switchBorderSpecie(false)
            especie = false
            binding.imageEspecieCachorro.isClickable = false
            binding.imageEspecieGato.isClickable = true
            Log.d("CadastroPetFragment", "Espécie: Cachorro")
        }

        binding.buttonAvancarCadastroPet.setOnClickListener {
            if (binding.nomeCadastroPet.text.isEmpty()) {
                toast("Preencha o nome do pet", requireContext())
                return@setOnClickListener
            }

            if (defineSex(binding.spinnerSexoPet.selectedItem.toString()) == "n") {
                toast("Preencha o sexo do pet", requireContext())
                return@setOnClickListener
            }

            if (binding.idadePet.text.isEmpty()) {
                toast("Preencha a idade do pet", requireContext())
                return@setOnClickListener
            }

            val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
            val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(dialogView)
                .create()

            val buttonSim = dialogView.findViewById<Button>(R.id.dialogBtnSim)
            val buttonNao = dialogView.findViewById<Button>(R.id.dialogBtnNao)

            buttonSim.setOnClickListener {
                val pet = createPetDTO()
                savePet(pet) {
                    findNavController().navigate(R.id.action_cadastroPetFragment_to_listPetFragment)
                }
                dialog.dismiss()
            }

            buttonNao.setOnClickListener {
                val pet = createPetDTO()
                savePet(pet) {
                    findNavController().navigate(R.id.action_cadastroPetFragment_to_indexActivity)
                }
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        binding.buttonVoltarEdit.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun createPetDTO(): PetDTO {
        val sharedPreferences = requireContext().getSharedPreferences("user_token", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "") ?: ""

        return PetDTO(
            idCliente = userId,
            idAnimal = null,
            nome = binding.nomeCadastroPet.text.toString(),
            gato = especie,
            idade = binding.idadePet.text.toString().toIntOrNull(),
            macho = binding.spinnerSexoPet.selectedItem.toString() == "Macho"
        )
    }

    private fun savePet(pet: PetDTO, onSuccess: () -> Unit) {
        Log.d("CadastroPetFragment", "Pet a ser salvo: $pet")

        retrofitClient.api.createPet(pet).enqueue(object : Callback<PetDTO> {
            override fun onResponse(call: Call<PetDTO>, response: Response<PetDTO>) {
                if (response.isSuccessful) {
                    toast("Pet salvo com sucesso", requireContext())
                    // Notificar que um novo pet foi criado
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "pet_created",
                        true
                    )
                    // Navegar para a lista de pets
                    findNavController().navigate(R.id.action_cadastroPetFragment_to_listPetFragment)
                } else {
                    Log.e("CadastroPetFragment", "Erro ao salvar pet: ${response.errorBody()?.string()}")
                    toast("Erro ao salvar pet", requireContext())
                }
            }

            override fun onFailure(call: Call<PetDTO>, t: Throwable) {
                toast("Falha na conexão: ${t.message}", requireContext())
            }
        })
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

    private fun defineSex(sexo: String): String {
        return when (sexo) {
            "Macho" -> "m"
            "Fêmea" -> "f"
            else -> "n"
        }
    }
}
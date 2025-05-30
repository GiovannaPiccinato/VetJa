package com.example.vetJa.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.vetJa.R
import com.example.vetJa.activitys.LoginActivity
import com.example.vetJa.databinding.FragmentCadastroPetBinding
import com.example.vetJa.models.Pet.PetDTO
import com.example.vetJa.models.login.LoginResponse
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.models.user.UserResponse
import com.example.vetJa.retroClient.RetrofitClient
import com.example.vetJa.utils.toast

class CadastroPetFragment : Fragment() {

    private lateinit var binding: FragmentCadastroPetBinding
    private lateinit var retrofitClient: RetrofitClient
    private var especie: Boolean = false
    var usuario: UserDTO? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            usuario = UserDTO(
                idCliente = null,
                nome = args.getString("nome"),
                senha = args.getString("senha"),
                email = args.getString("email"),
                cpf = args.getString("cpf"),
                telefone = args.getString("telefone"),
            )
        } ?: Log.d("CadastroPetFragment", "Arguments are null")

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
                saveUser(usuario)
                Log.d("CustomDialog", "Entrada do usuário: Confirmar")
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                dialog.dismiss()
            }

            buttonNao.setOnClickListener {
                saveUser(usuario)
                Log.d("CustomDialog", "Entrada do usuário: Cancelar")
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

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

    private fun defineSex(sexo: String): String {
        return when (sexo) {
            "Macho" -> "m"
            "Fêmea" -> "f"
            else -> "n"
        }
    }

    private fun saveUser(usuario: UserDTO?) {

        Log.d("CadastroPetFragment", "Usuário: $usuario")
        val apiService = retrofitClient.api
        apiService.createUser(usuario!!).enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: retrofit2.Call<UserResponse>,
                response: retrofit2.Response<UserResponse>
            ) {
                val userResponse = response.body()
                if (response.isSuccessful && userResponse != null) {
                    val sharedPreferences = requireContext().getSharedPreferences("user_token", Context.MODE_PRIVATE)
                    sharedPreferences.edit {
                        putString("user_token", userResponse.signIn.token)
                    }

                    Log.d("token-shared", sharedPreferences.getString("user_token", "").toString())

                    toast("Olá, ${userResponse.signIn.user.nome}, Seja bem-vindo(a)!", requireContext())

                    val pet = PetDTO(
                        idCliente = userResponse.signIn.user.idCliente,
                        idAnimal = null,
                        nome = binding.nomeCadastroPet.text.toString(),
                        idade = binding.idadePet.text.toString(),
                        raca = if (especie) "Gato" else "Cachorro"
                    )
                    savePet(pet)
                    toast("Usuário salvo com sucesso", requireContext())
                } else {
                    Log.e("CadastroPetFragment", "Erro ao salvar usuário: ${response.errorBody()?.string()}")
                    toast("Erro ao salvar usuário: resposta inválida", requireContext())
                }
            }

            override fun onFailure(call: retrofit2.Call<UserResponse>, t: Throwable) {
                toast("Falha na requisição: ${t.message}", requireContext())
            }
        })
    }

    private fun savePet(pet: PetDTO) {
        Log.d("CadastroPetFragment", "Pet salvo: $pet")
        Log.d("CadastroPetFragment", "Usuário ID: ${usuario?.idCliente}")
        Log.d("CadastroPetFragment", "Pet com ID do cliente: $pet")

        val apiService = retrofitClient.api
        apiService.createPet(pet).enqueue(object : retrofit2.Callback<PetDTO> {
            override fun onResponse(
                call: retrofit2.Call<PetDTO>,
                response: retrofit2.Response<PetDTO>
            ) {
                if (response.isSuccessful) {
                    toast("Pet salvo com sucesso", requireContext())
                } else {
                    Log.e("CadastroPetFragment", "Erro ao salvar pet: ${response.errorBody()?.string()}")
                    toast("Erro ao salvar pet", requireContext())
                }
            }

            override fun onFailure(call: retrofit2.Call<PetDTO>, t: Throwable) {
                toast("Falha na requisição: ${t.message}", requireContext())
            }
        })
    }
}
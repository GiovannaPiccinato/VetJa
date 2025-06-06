package com.example.vetJa.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vetJa.databinding.FragmentEditUsuarioBinding
import com.example.vetJa.models.user.User
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.retroClient.RetrofitClient
import com.example.vetJa.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditUsuarioFragment : Fragment() {

    lateinit var binding: FragmentEditUsuarioBinding
    private lateinit var ususario: UserDTO

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditUsuarioBinding.inflate(inflater, container, false)
        ususario = UserDTO(
            nome = "carregando...",
            email = "carregando...",
            telefone = "carregando...",
            senha = "**********",
            idCliente = null // ID do cliente deve ser definido corretamente
        )
        getUsuario()
        return binding.root
    }

    private fun getUsuario() {
        val retroFitclient = RetrofitClient(requireContext().applicationContext)

        retroFitclient.api.getUserDTOById().enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val userDTO = response.body()
                    if (userDTO != null) {
                        binding.editNomeAtualizaUser.setText(userDTO.nome)
                        binding.editEmailAtualizaUser.setText(userDTO.email)
                        binding.editTelAtualizaUser.setText(userDTO.telefone)
                        binding.editSenhaAtualizaUser.setText(ususario.senha)

                        binding.buttonAvancarEdit.setOnClickListener {
                            updateUsuario(userDTO.idCliente)
                        }
                    } else {
                        toast("Usuário não encontrado", requireContext())
                    }
                } else {
                    toast("Erro ao buscar usuário", requireContext())
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                toast("Erro de conexão", requireContext())
            }
        })
    }

    fun updateUsuario(idCliente: String?) {
        val retroFitclient = RetrofitClient(requireContext().applicationContext)

        ususario = UserDTO(
            nome = binding.editNomeAtualizaUser.text.toString(),
            email = binding.editEmailAtualizaUser.text.toString(),
            telefone = binding.editTelAtualizaUser.text.toString(),
            senha = if (binding.editSenhaAtualizaUser.text.toString() != "**********") null else binding.editSenhaAtualizaUser.text.toString(),
            idCliente = idCliente // ID do cliente deve ser definido corretamente
        )

        retroFitclient.api.updateUser(ususario).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val updatedUser = response.body()
                    if (updatedUser != null) {
                        toast("Usuário atualizado com sucesso", requireContext())

                    } else {
                        toast("Erro ao atualizar usuário", requireContext())
                    }
                } else {
                    toast("Erro ao atualizar usuário", requireContext())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                toast("Erro de conexão", requireContext())
                Log.e("EditUsuarioFragment", "Erro ao atualizar usuário: ${t.message}")
            }
        })
    }

}
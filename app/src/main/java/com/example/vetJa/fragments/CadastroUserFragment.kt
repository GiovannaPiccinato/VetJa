package com.example.vetJa.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vetJa.R
import com.example.vetJa.databinding.FragmentCadastroUserBinding
import com.example.vetJa.models.user.UserDTO

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CadastroUserFragment : Fragment() {

    private lateinit var binding: FragmentCadastroUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCadastroUserBinding.inflate(inflater, container, false)
        binding.buttonAvancarCadastro.setOnClickListener {
            guardarUsuario()
        }

        return binding.root
    }

    private fun guardarUsuario() {

        if (binding.nomeCadastroUser.text.isNullOrEmpty()) {
            binding.nomeCadastroUser.error = "Campo nome é obrigatório"
            return
        }

        if (binding.senhaCadastroUser.text.isNullOrEmpty()) {
            binding.senhaCadastroUser.error = "Campo senha é obrigatório"
            return
        }

        if (binding.telCadastroUser.text.isNullOrEmpty()) {
            binding.telCadastroUser.error = "Campo telefone é obrigatório"
            return
        }

        if (binding.emailCadastroUser.text.isNullOrEmpty()) {
            binding.emailCadastroUser.error = "Campo email é obrigatório"
            return
        }

        if (binding.emailCadastroUser.text.toString().isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailCadastroUser.text.toString()).matches()) {
            binding.emailCadastroUser.error = "Email inválido"
            return
        }

        if (binding.cepCadastroUser.text.isNullOrEmpty()) {
            binding.cepCadastroUser.error = "Campo cep é obrigatório"
            return
        }

        if (binding.numeroCasaCadastroUser.text.toString().isEmpty()) {
            binding.numeroCasaCadastroUser.error = "O número da casa deve ter pelo menos 1 dígito"
            return
        }

        if (binding.numeroCasaCadastroUser.text.isNullOrEmpty()) {
            binding.numeroCasaCadastroUser.error = "Campo número da casa é obrigatório"
            return
        }

        if (binding.senhaCadastroUser.text.toString().length < 6) {
            binding.senhaCadastroUser.error = "A senha deve ter pelo menos 6 caracteres"
            return
        }

        if (binding.telCadastroUser.text.toString().length < 10) {
            binding.telCadastroUser.error = "O telefone deve ter pelo menos 10 dígitos"
            return
        }

        if (binding.cepCadastroUser.text.toString().length < 8) {
            binding.cepCadastroUser.error = "O CEP deve ter pelo menos 8 dígitos"
            return
        }

        val usuario = UserDTO(
            nome = binding.nomeCadastroUser.text.toString(),
            senha = binding.senhaCadastroUser.text.toString(),
            telefone = binding.telCadastroUser.text.toString(),
            email = binding.emailCadastroUser.text.toString(),
            cep = binding.cepCadastroUser.text.toString(),
            endereco = binding.numeroCasaCadastroUser.text.toString(),
            idCliente = null,
            cpf = null
        )

        val bundle = Bundle().apply {
            with(usuario) {
                putString("nome", nome)
                putString("senha", senha)
                putString("telefone", telefone)
                putString("email", email)
                putString("cep", cep)
                putString("endereco", endereco)
                putString("idCliente", idCliente)
                putString("cpf", cpf)
            }
        }

        val navController = findNavController()
        navController.navigate(R.id.action_cadastroUserFragment_to_cadastroPetFragment, bundle)

    }
}
package com.example.vetJa.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vetJa.R
import com.example.vetJa.databinding.FragmentCadastroUserBinding
import com.example.vetJa.models.user.UserDTO

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

        if (binding.senhaCadastroUser.text.toString().length < 6) {
            binding.senhaCadastroUser.error = "A senha deve ter pelo menos 6 caracteres"
            return
        }

        if (binding.telCadastroUser.text.isNullOrEmpty()) {
            binding.telCadastroUser.error = "Campo telefone é obrigatório"
            return
        }

        if (binding.telCadastroUser.text.toString().length < 10) {
            binding.telCadastroUser.error = "O telefone deve ter pelo menos 10 dígitos"
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

        //mudar depois
        if (binding.cepCadastroUser.text.isNullOrEmpty()) {
            binding.cepCadastroUser.error = "Campo CPF é obrigatório"
            return
        }

        if (binding.cepCadastroUser.text.toString().length < 12) {
            binding.cepCadastroUser.error = "O CPF deve ter pelo menos 8 dígitos"
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



        val usuario = UserDTO(
            nome = binding.nomeCadastroUser.text.toString(),
            senha = binding.senhaCadastroUser.text.toString(),
            email = binding.emailCadastroUser.text.toString(),
//            cep = binding.cepCadastroUser.text.toString(),
//            endereco = binding.numeroCasaCadastroUser.text.toString(),
            telefone = binding.telCadastroUser.text.toString(),
            cpf = binding.cepCadastroUser.text.toString(), // CPF is not provided in the form, using a placeholder
            idCliente = null,
        )

        val bundle = Bundle().apply {
            with(usuario) {
                putString("nome", nome)
                putString("senha", senha)
                putString("email", email)
                putString("telefone", telefone)
                putString("cpf", cpf)
//                putString("cep", cep)
//                putString("endereco", endereco)
                putString("idCliente", idCliente)
            }
        }

        val navController = findNavController()
        navController.navigate(R.id.action_cadastroUserFragment_to_cadastroPetFragment, bundle)

    }
}
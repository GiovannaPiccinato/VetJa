package com.example.vetJa.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.vetJa.R
import com.example.vetJa.activitys.LoginActivity
import com.example.vetJa.databinding.FragmentCadastroPetBinding
import com.example.vetJa.models.PetDTO
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.utils.toast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CadastroPetFragment : Fragment() {

    private lateinit var binding: FragmentCadastroPetBinding
    private var especie: Boolean = false
    var usuario: UserDTO? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            usuario = UserDTO(
                idCliente = null,
                cpf = args.getString("cpf"),
                nome = args.getString("nome"),
                senha = args.getString("senha"),
                telefone = args.getString("telefone"),
                email = args.getString("email"),
                cep = args.getString("cep"),
                endereco = args.getString("endereco"),
            )
        } ?: Log.d("CadastroPetFragment", "Arguments are null")

        binding = FragmentCadastroPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageEspecieGato.setOnClickListener {
            swichBorderEspecie(true)
            especie = true
            binding.imageEspecieGato.isClickable = false
            binding.imageEspecieCachorro.isClickable = true
            Log.d("CadastroPetFragment", "Espécie: Gato")
        }

        binding.imageEspecieCachorro.setOnClickListener {
            swichBorderEspecie(false)
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

            if (binding.sexoPet.text.isEmpty()) {
                toast("Preencha o sexo do pet", requireContext())
                return@setOnClickListener
            }

            if (binding.idadePet.text.isEmpty()) {
                toast("Preencha a idade do pet", requireContext())
                return@setOnClickListener
            }

            val pet = PetDTO(
                id = null,
                nome = binding.nomeCadastroPet.text.toString(),
                sexo = binding.sexoPet.text.toString(),
                idade = binding.idadePet.text.toString(),
                isCat = especie
            )

            val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

            val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(dialogView)
                .create()

            val buttonSim = dialogView.findViewById<Button>(R.id.dialogBtnSim)
            val buttonNao = dialogView.findViewById<Button>(R.id.dialogBtnNao)

            buttonSim.setOnClickListener {
                saveUser(usuario)
                savePet(pet)

                Log.d("CustomDialog", "Entrada do usuário: Confirmar")
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                dialog.dismiss()


            }

            buttonNao.setOnClickListener {
                saveUser(usuario)
                savePet(pet)

                Log.d("CustomDialog", "Entrada do usuário: Cancelar")
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

        }
    }

    fun swichBorderEspecie(isCat: Boolean) {
        if (isCat) {
            binding.imageEspecieGato.setBackgroundResource(R.drawable.border_selected)
            binding.imageEspecieCachorro.setBackgroundResource(R.drawable.border_default)
        } else {
            binding.imageEspecieCachorro.setBackgroundResource(R.drawable.border_selected)
            binding.imageEspecieGato.setBackgroundResource(R.drawable.border_default)
        }
    }

    private fun saveUser(usuario: UserDTO?) {

    }

    private fun savePet(pet: PetDTO) {
        // Implementar a lógica para salvar o pet
        Log.d("CadastroPetFragment", "Pet salvo: $pet")
    }



}
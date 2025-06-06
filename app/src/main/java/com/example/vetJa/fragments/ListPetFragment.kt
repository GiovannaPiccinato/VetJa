package com.example.vetJa.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.R
import com.example.vetJa.adapters.PetAdapter
import com.example.vetJa.models.Pet.Pet
import com.example.vetJa.models.Pet.PetDTO
import com.example.vetJa.models.user.User
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.retroClient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.addAll
import kotlin.text.clear

class ListPetFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PetAdapter
    private val pet = mutableListOf<PetDTO>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListPetFragment", "onViewCreated chamado")

        recyclerView = view.findViewById(R.id.rvPetList)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
        adapter = PetAdapter(
                pet,
                onEditClick = { selectedPet ->

                    Log.d("ListPetFragment", "onEditClick chamado para o pet: ${selectedPet.idAnimal}")

                    // vai para EditPetFragment
                    val fragment = EditPetFragment()

                    // Enviamos o pet como argumento
                    val bundle = Bundle()
                    selectedPet.idAnimal?.let { petId ->
                        bundle.putString("petId", petId)
                        Log.d("ListPetFragment", "ID do pet selecionado: $petId")
                    } ?: Log.e("ListPetFragment", "ID do pet é nulo")
                    fragment.arguments = bundle

                    // Troca o fragmento
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                },
            onDeleteClick = { selectedPet ->
                val apiService = RetrofitClient(requireContext()).api
                val petId = selectedPet.idAnimal.toString()// pega o ID que deseja excluir

                val call = apiService.deletePet(petId)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            pet.remove(selectedPet) // Remove o pet da lista
                            adapter.notifyDataSetChanged() // Atualiza o RecyclerView
                            Toast.makeText(
                                requireContext(),
                                "Pet deletado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Erro ao deletar o pet.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<Void>,
                        t: Throwable
                    ) {     // Se houve falha na chamada, como falta de internet ou erro de conexão.
                        Toast.makeText(requireContext(), "Falha na conexão.", Toast.LENGTH_SHORT)
                            .show()  // Mostra uma mensagem avisando que houve problema na comunicação com a API.
                    }
                })
            }

        )
        recyclerView.adapter = adapter
        Log.d("ListPetFragment", "RecyclerView e Adapter configurados")


        val apiService = RetrofitClient(requireContext()).api
        Log.d("ListPetFragment", "Iniciando chamada para getListPets()")

        apiService.getUserDTOById()
            .enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()
                        if (user != null) {
                            apiService.getListPets(user.idCliente.toString())
                                .enqueue(object : Callback<List<PetDTO>> {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onResponse(
                                        call: Call<List<PetDTO>>,
                                        response: Response<List<PetDTO>>
                                    ) {
                                        Log.d(
                                            "ListPetFragment",
                                            "onResponse chamado: code=${response.code()}"
                                        )
                                        if (response.isSuccessful && response.body() != null) {
                                            pet.clear()
                                            pet.addAll(response.body()!!)
                                            adapter.notifyDataSetChanged()
                                            Log.d(
                                                "ListPetFragment",
                                                "Serviços carregados: ${pet.size}"
                                            )
                                        } else {
                                            val errorMessage = when (response.code()) {
                                                400 -> "Requisição inválida."
                                                401 -> "Não autorizado. Verifique suas credenciais."
                                                404 -> "Serviços não encontrados."
                                                500 -> "Erro no servidor. Tente novamente mais tarde."
                                                else -> "Erro desconhecido: ${response.code()}"
                                            }
                                            Log.e(
                                                "ListPetFragment",
                                                "Erro na resposta: $errorMessage"
                                            )
                                            Toast.makeText(
                                                requireContext(),
                                                errorMessage,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<List<PetDTO>>, t: Throwable) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Falha na conexão: ${t.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Usuário não carregado.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao obter usuário",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erro: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

}

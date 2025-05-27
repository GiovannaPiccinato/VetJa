package com.example.vetJa.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.glance.visibility
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetJa.adapters.VetAdapter
import com.example.vetJa.databinding.FragmentVetListBinding
import com.example.vetJa.models.Veterinario
import com.example.vetJa.retroClient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.addAll
import kotlin.text.clear

class VetListFragment : Fragment() {

    private var _binding: FragmentVetListBinding? = null
    private val binding get() = _binding!!

    private lateinit var vetAdapter: VetAdapter
    private val veterinarios = mutableListOf<Veterinario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("VetListFragment", "onViewCreated chamado")
        setupRecyclerView()
        loadVeterinarios()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewVets.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVets.setHasFixedSize(true)

        vetAdapter = VetAdapter(veterinarios) { selectedVeterinario ->
            Toast.makeText(requireContext(), "Veterinário: ${selectedVeterinario.nome}", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewVets.adapter = vetAdapter
        Log.d("VetListFragment", "RecyclerView e Adapter configurados")
    }

    private fun loadVeterinarios() {
        binding.progressBarVets.visibility = View.VISIBLE
        binding.textViewErrorVets.visibility = View.GONE
        binding.recyclerViewVets.visibility = View.GONE

        val apiService = RetrofitClient(requireContext()).api
        Log.d("VetListFragment", "Iniciando chamada para getVeterinarios()")

        apiService.getVeterinarios().enqueue(object : Callback<List<Veterinario>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Veterinario>>, response: Response<List<Veterinario>>) {
                binding.progressBarVets.visibility = View.GONE
                Log.d("VetListFragment", "onResponse chamado: code=${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    veterinarios.clear()
                    veterinarios.addAll(response.body()!!)
                    vetAdapter.notifyDataSetChanged()

                    if (veterinarios.isEmpty()) {
                        binding.textViewErrorVets.text = "Nenhum veterinário encontrado."
                        binding.textViewErrorVets.visibility = View.VISIBLE
                        binding.recyclerViewVets.visibility = View.GONE
                    } else {
                        binding.recyclerViewVets.visibility = View.VISIBLE
                        binding.textViewErrorVets.visibility = View.GONE
                    }
                    Log.d("VetListFragment", "Veterinários carregados: ${veterinarios.size}")
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Requisição inválida."
                        401 -> "Não autorizado. Verifique suas credenciais."
                        403 -> "Acesso proibido."
                        404 -> "Veterinários não encontrados."
                        500 -> "Erro no servidor. Tente novamente mais tarde."
                        else -> "Erro ao carregar veterinários: ${response.code()} - ${response.message()}"
                    }
                    Log.e("VetListFragment", "Erro na resposta: $errorMessage")
                    binding.textViewErrorVets.text = errorMessage
                    binding.textViewErrorVets.visibility = View.VISIBLE
                    binding.recyclerViewVets.visibility = View.GONE
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Veterinario>>, t: Throwable) {
                binding.progressBarVets.visibility = View.GONE
                Log.e("VetListFragment", "Falha na conexão: ${t.message}", t)
                val failMessage = "Falha na conexão: ${t.localizedMessage ?: "Erro desconhecido"}"
                binding.textViewErrorVets.text = failMessage
                binding.textViewErrorVets.visibility = View.VISIBLE
                binding.recyclerViewVets.visibility = View.GONE
                Toast.makeText(requireContext(), failMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewVets.adapter = null
        _binding = null
    }
}
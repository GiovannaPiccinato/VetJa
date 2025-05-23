package com.example.vetJa.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.R
import com.example.vetJa.adapters.ServicoAdapter
import com.example.vetJa.models.service.Service
import com.example.vetJa.retroClient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServicoAdapter
    private val servicos = mutableListOf<Service>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated chamado")

        recyclerView = view.findViewById(R.id.recyclerServicos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
        adapter = ServicoAdapter(servicos)
        recyclerView.adapter = adapter
        Log.d("HomeFragment", "RecyclerView e Adapter configurados")

        val apiService = RetrofitClient(requireContext()).api
        Log.d("HomeFragment", "Iniciando chamada para getAllServices()")

        apiService.getAllServices().enqueue(object : Callback<List<Service>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                Log.d("HomeFragment", "onResponse chamado: code=${response.code()}")
                if (response.isSuccessful && response.body() != null) {
                    servicos.clear()
                    servicos.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                    Log.d("HomeFragment", "Serviços carregados: ${servicos.size}")
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Requisição inválida."
                        401 -> "Não autorizado. Verifique suas credenciais."
                        404 -> "Serviços não encontrados."
                        500 -> "Erro no servidor. Tente novamente mais tarde."
                        else -> "Erro desconhecido: ${response.code()}"
                    }
                    Log.e("HomeFragment", "Erro na resposta: $errorMessage")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
                Log.e("HomeFragment", "Falha na conexão: ${t.message}", t)
                Toast.makeText(requireContext(), "Falha na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


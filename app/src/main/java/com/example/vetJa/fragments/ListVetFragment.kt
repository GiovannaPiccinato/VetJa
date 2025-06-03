package com.example.vetJa.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.API.GeoClient
import com.example.vetJa.R
import com.example.vetJa.adapters.VetAdapter
import com.example.vetJa.models.GeoResponse
import com.example.vetJa.models.Vet.VetDTO
import com.example.vetJa.retroClient.RetrofitClient
import com.example.vetJa.utils.LocationUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VetListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewError: TextView
    private lateinit var vetAdapter: VetAdapter
    private val veterinarios = mutableListOf<VetDTO>()
    private val userCep = "01001-000" // Substitua conforme necessário

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list_vet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewVets)
        progressBar = view.findViewById(R.id.progressBarVets)
        textViewError = view.findViewById(R.id.textViewErrorVets)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        vetAdapter = VetAdapter(veterinarios) { selectedVet ->
            Toast.makeText(requireContext(), "Veterinário: ${selectedVet.nome}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = vetAdapter

        loadVeterinarios()
    }

    private fun loadVeterinarios() {
        progressBar.visibility = View.VISIBLE
        textViewError.visibility = View.GONE
        recyclerView.visibility = View.GONE

        val apiService = RetrofitClient(requireContext()).api

        // Coordenadas do usuário
        GeoClient.api.getCoordinates(userCep).enqueue(object : Callback<List<GeoResponse>> {
            override fun onResponse(call: Call<List<GeoResponse>>, responseUser: Response<List<GeoResponse>>) {
                if (responseUser.isSuccessful && responseUser.body() != null) {
                    val userLocation = responseUser.body()!![0]
                    val userLat = userLocation.lat.toDouble()
                    val userLon = userLocation.lon.toDouble()

                    // Carregar veterinários
                    apiService.getVeterinarios().enqueue(object : Callback<List<VetDTO>> {
                        override fun onResponse(
                            call: Call<List<VetDTO>>,
                            response: Response<List<VetDTO>>
                        ) {
                            progressBar.visibility = View.GONE

                            if (response.isSuccessful && response.body() != null) {
                                val todosVets = response.body()!!
                                val vetsDentroRaio = mutableListOf<VetDTO>()

                                var carregados = 0
                                val restantes = todosVets.size

                                todosVets.forEach { vet ->
                                    val cepVet = vet.cep ?: ""
                                    if (cepVet.isNotBlank()) {
                                        GeoClient.api.getCoordinates(cepVet)
                                            .enqueue(object : Callback<List<GeoResponse>> {
                                                override fun onResponse(
                                                    call: Call<List<GeoResponse>>,
                                                    responseVetGeo: Response<List<GeoResponse>>
                                                ) {
                                                    carregados++
                                                    if (responseVetGeo.isSuccessful && responseVetGeo.body()?.isNotEmpty() == true) {
                                                        val vetLoc = responseVetGeo.body()!![0]
                                                        val vetLat = vetLoc.lat.toDouble()
                                                        val vetLon = vetLoc.lon.toDouble()

                                                        val distancia = LocationUtils.calcularDistanciaKm(
                                                            userLat, userLon, vetLat, vetLon
                                                        )

                                                        if (distancia <= 15.0) {
                                                            vetsDentroRaio.add(vet)
                                                        }
                                                    }
                                                    if (carregados == restantes) {
                                                        atualizarRecycler(vetsDentroRaio)
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<List<GeoResponse>>,
                                                    t: Throwable
                                                ) {
                                                    carregados++
                                                    if (carregados == restantes) {
                                                        atualizarRecycler(vetsDentroRaio)
                                                    }
                                                }
                                            })
                                    } else {
                                        carregados++
                                        if (carregados == restantes) {
                                            atualizarRecycler(vetsDentroRaio)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<VetDTO>>, t: Throwable) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Erro ao carregar veterinários", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<List<GeoResponse>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Erro ao buscar localização do usuário", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun atualizarRecycler(vets: List<VetDTO>) {
        veterinarios.clear()
        veterinarios.addAll(vets)
        vetAdapter.notifyDataSetChanged()

        if (veterinarios.isEmpty()) {
            textViewError.text = "Nenhum veterinário próximo encontrado."
            textViewError.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            textViewError.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}

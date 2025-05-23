package com.example.vetJa.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vetJa.R
import com.example.vetJa.models.user.User
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.retroClient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnEditProfile: LinearLayout
    private lateinit var btnUserInfo: LinearLayout
    private lateinit var retrofitClient: RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Inicializa Retrofit
        retrofitClient = RetrofitClient(requireContext().applicationContext)

        // Referências das views
        tvUserName = view.findViewById(R.id.tv_user_name)
        tvUserEmail = view.findViewById(R.id.tv_user_email)
        btnUserInfo = view.findViewById(R.id.btn_view_data)
        btnEditProfile = view.findViewById(R.id.btn_edit_profile)

        btnUserInfo.setOnClickListener {
            userInfo()
        }
        btnEditProfile.setOnClickListener {
            editUserInfo()
        }

        // Chamada da API
        getUserData()

        return view
    }

    private fun getUserData() {

            retrofitClient.api.getUserById()
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            tvUserName.text = user?.nome ?: "Sem nome"
                            tvUserEmail.text = user?.email ?: "Sem email"
                        } else {
                            Toast.makeText(context, "Erro ao obter usuário", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

    }

    private fun userInfo () {

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, EditUsuarioFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun editUserInfo() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, EditUsuarioFragment())
            .addToBackStack(null)
            .commit()
    }

}

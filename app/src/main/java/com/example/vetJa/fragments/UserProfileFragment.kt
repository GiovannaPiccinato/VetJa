package com.example.vetJa.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.vetJa.R
import com.example.vetJa.activitys.IntroducaoActivity
import com.example.vetJa.models.login.LoginResponse
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.retroClient.RetrofitClient
import com.example.vetJa.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileFragment() : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnEditProfile: LinearLayout
    private lateinit var btnUserInfo: LinearLayout
    private lateinit var btnLogout: LinearLayout
    private lateinit var btnDeleteAccount: LinearLayout
    private var user: UserDTO? = null

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
        btnLogout = view.findViewById(R.id.btn_logout)
        btnDeleteAccount = view.findViewById(R.id.btn_delete_account)

        getUserData()

        btnUserInfo.setOnClickListener {
            userInfo()
        }

        btnEditProfile.setOnClickListener {
            editUserInfo()
        }

        btnLogout.setOnClickListener {
            logout()
        }

        btnDeleteAccount.setOnClickListener {
            getUserData()
            deleteUser(user!!)
        }

        return view

    }

    private fun getUserData() {
        retrofitClient.api.getUserDTOById()
            .enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        user = response.body()
                        Log.d("UserProfileFragment", "Usuário obtido: $user")
                        tvUserName.text = user?.nome ?: "Sem nome"
                        tvUserEmail.text = user?.email ?: "Sem email"
                    } else {
                        Toast.makeText(context, "Erro ao obter usuário", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun userInfo() {
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

    fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("user_token", MODE_PRIVATE)
        sharedPreferences.edit {
            remove("user_token")
        }

        toast("Logout realizado com sucesso", requireContext())
        val intent = Intent(requireContext(), IntroducaoActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun deleteUser(user: UserDTO) {
        retrofitClient.api.deleteUser(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    toast("Conta excluída com sucesso", requireContext())
                    logout() // Chama o logout após excluir a conta
                } else {
                    Log.e("UserProfileFragment", "Erro ao excluir conta")
                    Toast.makeText(requireContext(), "Erro ao excluir conta", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("UserProfileFragment", "Falha na conexão: ${t.message}", t)
                Toast.makeText(
                    requireContext(),
                    "Falha na conexão: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}

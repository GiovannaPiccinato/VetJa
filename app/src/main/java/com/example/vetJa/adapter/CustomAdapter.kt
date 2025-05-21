package com.example.vetJa.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.models.user.User
import androidx.fragment.app.FragmentManager
import com.example.vetJa.fragments.AddDialogFragment
import com.example.vetJa.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CustomAdapter(private val dataSet: List<User>, private val fragmentManager: FragmentManager, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nome)
        val email: TextView = view.findViewById(R.id.email)
        val updtBtn: Button = view.findViewById(R.id.editarVet)
        val deleteBtn: Button = view.findViewById(R.id.deleteVet)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.model_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = dataSet[position]
        viewHolder.nome.text = user.nome
        viewHolder.email.text = user.email

        viewHolder.updtBtn.setOnClickListener {
            updateVet(viewHolder);
        }

        viewHolder.deleteBtn.setOnClickListener {
            deleteVet(viewHolder);
        }
    }

    private val logging = HttpLoggingInterceptor { message ->
        Log.d("OkHttp", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.15.17:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private fun updateVet(viewHolder: ViewHolder) {
        val dialog = AddDialogFragment();
        val bundle = Bundle();
        bundle.putString("email", viewHolder.email.text.toString());
        dialog.arguments = bundle;
        dialog.show(fragmentManager, dialog.tag);
    }

    private fun deleteVet(viewHolder: ViewHolder) {
//        val apiService = retrofit.create(ApiService::class.java);
//
//        val call = apiService.deleteUser(UserDTO(null, viewHolder.email.text.toString(), null, null, null));
//
//        call.enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//
//                if (response.isSuccessful && response.body() != null) {
//                    val loginResponses = response.body()!!
//                    if (loginResponses.status) {
//                        Toast.makeText(viewHolder.itemView.context, loginResponses.message.toString(), Toast.LENGTH_LONG).show()
//
//                        recreate(activity);
//                    } else {
//                        Toast.makeText(viewHolder.itemView.context, loginResponses.message.toString(), Toast.LENGTH_LONG).show()
//                    }
//                } else {
//                    Toast.makeText(viewHolder.itemView.context, "Erro ao criar usuário!", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(viewHolder.itemView.context, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
//            }
//        })
    }

    override fun getItemCount() = dataSet.size
}

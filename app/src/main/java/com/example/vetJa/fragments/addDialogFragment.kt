import androidx.fragment.app.DialogFragment

//package com.example.vetJa.fragments
//
//import com.example.vetJa.API.ApiService
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.DialogFragment
//import com.example.vetJa.R
//import com.example.vetJa.models.login.LoginResponse
//import com.example.vetJa.models.user.User
//import com.example.vetJa.models.user.UserDTO
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
public class AddDialogFragment : DialogFragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.dialog_create, container)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val saveButton = view.findViewById<Button>(R.id.dialog_save_btn);
//        val name = view.findViewById<TextView>(R.id.dialog_name);
//        val email = view.findViewById<TextView>(R.id.dialog_email);
//        val cpf = view.findViewById<TextView>(R.id.dialog_cpf);
//        val idCliente = view.findViewById<TextView>(R.id.dialog_cliente);
//        val pass = view.findViewById<TextView>(R.id.dialog_pass);
//
//        val userEmail = arguments?.getString("email");
//
//        if(userEmail.toString().isNotEmpty() && userEmail.toString() !== "") {
//            val apiService = retrofit.create(ApiService::class.java);
//
//            val dto = toDto(userEmail.toString(), null, null, null, null, null)
//
//            val call = apiService.getUserByEmail(dto)
//
//            call.enqueue(object : Callback<User> {
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//
//                    if (response.isSuccessful && response.body() != null) {
//                        val loginResponses = response.body()!!
//                        if (loginResponses.idCliente.isNotEmpty()) {
//                            name.text = loginResponses.nome;
//                            email.text = loginResponses.email;
//                            cpf.text = loginResponses.cpf;
//                            pass.text = loginResponses.senha
//                            idCliente.text = loginResponses.idCliente
//                        } else {
//                            Toast.makeText(view.context, "Falha ao carregar as informações de usuário", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Toast.makeText(view.context, "Erro ao carregar usuário!", Toast.LENGTH_LONG).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<User>, t: Throwable) {
//                    Toast.makeText(view.context, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
//                }
//            })
//        }
//
//        saveButton.setOnClickListener {
//            if(userEmail.toString().isNotEmpty()) updateUser(view);
//            else addNewUser(view);
//        }
//    }
//
//    interface OnUserCreatedListener {
//        fun onUserCreated()
//    }
//
//    private var listener: OnUserCreatedListener? = null
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnUserCreatedListener) {
//            listener = context
//        }
//    }
//
//    private val logging = HttpLoggingInterceptor { message ->
//        Log.d("OkHttp", message)
//    }.apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(logging)
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .readTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(30, TimeUnit.SECONDS)
//        .build()
//
//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl("http://192.168.15.17:8000")
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(okHttpClient)
//        .build()
//
//    private fun addNewUser(view: View) {
//        val name = view.findViewById<TextView>(R.id.dialog_name).text;
//        val email = view.findViewById<TextView>(R.id.dialog_email).text;
//        val cpf = view.findViewById<TextView>(R.id.dialog_cpf).text;
//        val idCliente = view.findViewById<TextView>(R.id.dialog_cliente).text;
//        val pass = view.findViewById<TextView>(R.id.dialog_pass).text;
//
//        val dto = toDto(name.toString(), email.toString(), cpf.toString(), idCliente.toString(), pass.toString(), idCliente.toString());
//
//        val apiService = retrofit.create(ApiService::class.java);
//
//        val call = apiService.createUser(dto);
//
//        call.enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//
////                if (response.isSuccessful && response.body() != null) {
////                    val loginResponses = response.body()!!
////                    if (loginResponses.status) {
////                        Toast.makeText(view.context, loginResponses.message.toString(), Toast.LENGTH_LONG).show()
////
////                        listener?.onUserCreated()
////                        dismiss()
////                    } else {
////                        Toast.makeText(view.context, loginResponses.message.toString(), Toast.LENGTH_LONG).show()
////                    }
////                } else {
////                    Toast.makeText(view.context, "Erro ao criar usuário!", Toast.LENGTH_LONG).show()
////                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(view.context, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//    private fun updateUser(view: View) {
//        val name = view.findViewById<TextView>(R.id.dialog_name).text;
//        val email = view.findViewById<TextView>(R.id.dialog_email).text;
//        val cpf = view.findViewById<TextView>(R.id.dialog_cpf).text;
//        val idCliente = view.findViewById<TextView>(R.id.dialog_cliente).text;
//        val pass = view.findViewById<TextView>(R.id.dialog_pass).text;
//
//        val dto = toDto(name.toString(), email.toString(), cpf.toString(), idCliente.toString(), pass.toString(), idCliente.toString());
//
//        val apiService = retrofit.create(ApiService::class.java);
//
//        val call = apiService.updateUser(dto);
//
//        call.enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//
//                if (response.isSuccessful && response.body() != null) {
//                    val loginResponses = response.body()!!
//                    if (loginResponses.idCliente.toString().isNotEmpty()) {
//                        Toast.makeText(view.context, "Usuário Atualizado!", Toast.LENGTH_LONG).show()
//
//                        listener?.onUserCreated()
//                        dismiss()
//                    } else {
//                        Toast.makeText(view.context, "Falha ao atualizar o usuário!", Toast.LENGTH_LONG).show()
//                    }
//                } else {
//                    Toast.makeText(view.context, "Erro ao criar usuário!", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Toast.makeText(view.context, "Erro: ${t.message}", Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//
//    private fun toDto(email: String?, name: String?, cpf: String?, tel: String?, pass: String?, idCliente: String?) : UserDTO {
//        return UserDTO(name, email,cpf, pass, idCliente);
//    }
//
//
}
package API

import com.example.login.models.login.LoginResponse
import com.example.login.models.produtos.Produto
import com.example.login.models.user.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("/Login/login.php")
    fun login(
        @Query("usuario") usuario: String,
        @Query("senha") senha: String
    ): Call<List<LoginResponse>>


    @GET("/users/all")
    fun getAllUsers(): Call<List<User>>

}
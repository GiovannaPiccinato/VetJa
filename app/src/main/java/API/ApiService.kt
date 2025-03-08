package API

import com.example.login.models.login.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/Login/login.php")
    fun login(
        @Query("usuario") usuario: String,
        @Query("senha") senha: String
    ): Call<List<LoginResponse>>
}
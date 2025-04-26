package API

import com.example.login.models.login.LoginResponse
import com.example.login.models.user.User
import com.example.login.models.user.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @GET("/users/all")
    fun getAllUsers(): Call<List<User>>

    @GET("/users/user")
    fun getUserById(id: String): Call<User>

    @POST("/users/user/email")
    fun getUserByEmail(@Body dto: UserDTO): Call<User>

    @POST("/auth/signUp")
    fun createUser(@Body dto: UserDTO): Call<LoginResponse>

    @PUT("/users/user")
    fun updateUser(@Body dto: UserDTO): Call<User>

    @POST("/users/delete")
    fun deleteUser(@Body dto: UserDTO): Call<LoginResponse>;

}
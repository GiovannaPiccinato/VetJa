package com.example.vetJa.API

import com.example.vetJa.models.login.LoginRequest
import com.example.vetJa.models.login.LoginResponse
import com.example.vetJa.models.user.User
import com.example.vetJa.models.user.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @GET("/users/all")
    fun getAllUsers(): Call<List<User>>

    @GET("/users/user")
    fun getUserById(id: String): Call<User>

    @POST("/users/user/email")
    fun getUserByEmail(@Body dto: UserDTO): Call<User>

    @POST("/auth/signup")
    fun createUser(@Body dto: UserDTO): Call<LoginResponse>

    @POST("/auth/signin")
    fun login(@Body dto: LoginRequest): Call<LoginResponse>

    @PUT("/users/user")
    fun updateUser(@Body dto: UserDTO): Call<User>

    @POST("/users/delete")
    fun deleteUser(@Body dto: UserDTO): Call<LoginResponse>;

}
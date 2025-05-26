package com.example.vetJa.API

import com.example.vetJa.models.Pet.PetDTO
import com.example.vetJa.models.login.LoginRequest
import com.example.vetJa.models.login.LoginResponse
import com.example.vetJa.models.service.Service
import com.example.vetJa.models.user.User
import com.example.vetJa.models.user.UserDTO
import com.example.vetJa.models.user.UserResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Body

interface ApiService {

    @GET("/users/all")
    fun getAllUsers(): Call<List<User>>

    @GET("/users/user")
    fun getUserById(): Call<User>

    @GET("/services/all")
    fun getAllServices(): Call<List<Service>>

    @POST("/users/user/email")
    fun getUserByEmail(@Body dto: UserDTO): Call<User>

    @POST("/auth/signup")
    fun createUser(@Body dto: UserDTO): Call<UserResponse>

    @POST("/auth/signin")
    fun login(@Body dto: LoginRequest): Call<LoginResponse>

    @PUT("/users/user")
    fun updateUser(@Body dto: UserDTO): Call<User>

    @POST("/users/delete")
    fun deleteUser(@Body dto: UserDTO): Call<LoginResponse>;

    @GET("/pets/all")
    fun getListPets(): Call<List<PetDTO>>

    @POST("/animals/animal")
    fun createPet(@Body dto: PetDTO): Call<PetDTO>

}
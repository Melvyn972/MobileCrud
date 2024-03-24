package com.example.mobilecrud

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE

interface UserService {

    @GET("api/utilisateurs")
    fun getUsers(@Query("page") page: Int =1): Call<List<User>>

    @GET("api/utilisateurs/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @POST("api/utilisateurs")
    fun createUser(@Body user: User): Call<User>

    @PATCH("api/utilisateurs/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("api/utilisateurs/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>
}
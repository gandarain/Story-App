package com.dicoding.storyapp.api

import com.dicoding.storyapp.model.LoginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("/login")
    @Headers("Authorization: token")
    fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Call<LoginResponse>
}
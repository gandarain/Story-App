package com.dicoding.storyapp.api

import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.RegisterResponse
import com.dicoding.storyapp.model.StoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): Call<StoryResponse>
}
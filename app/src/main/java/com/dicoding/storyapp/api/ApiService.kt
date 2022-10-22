package com.dicoding.storyapp.api

import com.dicoding.storyapp.model.CreateStoryResponse
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.RegisterResponse
import com.dicoding.storyapp.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("stories")
    fun createStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float,
    ): Call<CreateStoryResponse>
}
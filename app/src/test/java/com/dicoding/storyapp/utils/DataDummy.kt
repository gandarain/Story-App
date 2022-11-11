package com.dicoding.storyapp.utils

import com.dicoding.storyapp.model.CreateStoryResponse
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.LoginResultResponse
import com.dicoding.storyapp.model.RegisterResponse

object DataDummy {
    fun generateDummyLoginSuccess(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResultResponse(
                userId = "userId",
                name = "name",
                token = "token"
            )
        )
    }

    fun generateDummyLoginError(): LoginResponse {
        return LoginResponse(
            error = true,
            message = "invalid password"
        )
    }

    fun generateDummyRegisterSuccess(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterError(): RegisterResponse {
        return RegisterResponse(
            error = true,
            message = "bad request"
        )
    }

    fun generateDummyCreateStorySuccess(): CreateStoryResponse {
        return CreateStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyCreateStoryError(): CreateStoryResponse {
        return CreateStoryResponse(
            error = true,
            message = "success"
        )
    }
}
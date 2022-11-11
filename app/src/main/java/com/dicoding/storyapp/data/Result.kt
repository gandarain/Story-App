package com.dicoding.storyapp.data

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val data: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
package com.dicoding.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postLogin(email: String, password: String) = storyRepository.login(email, password)
}
package com.dicoding.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postRegister(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}
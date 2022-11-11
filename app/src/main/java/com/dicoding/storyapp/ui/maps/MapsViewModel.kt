package com.dicoding.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStories()
}
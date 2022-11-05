package com.dicoding.storyapp.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.model.Story

class StoriesViewModel(repo: StoryRepository): ViewModel() {
    val getListStory: LiveData<PagingData<Story>> =
        repo.getListStories().cachedIn(viewModelScope)
}
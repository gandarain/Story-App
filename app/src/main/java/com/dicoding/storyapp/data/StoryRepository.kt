package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.preference.LoginPreference

class StoryRepository(private val pref: LoginPreference, private val apiService: ApiService) {
    fun getListStories(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(pref, apiService),
            pagingSourceFactory = {
                StoryPagingSource(pref, apiService)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: LoginPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}
package com.dicoding.storyapp.ui.stories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.model.StoryResponse
import com.dicoding.storyapp.preference.LoginPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val _listStories = MutableLiveData<List<Story>>()
    val listStories: LiveData<List<Story>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    init {
        getStories()
    }

    fun getStories() {
        val token = LoginPreference(context).getUser().token
        _isError.value = false
        _isLoading.value = true

        val client = token?.let {
            ApiConfig.getApiService().getStories(token = "Bearer $it", page = 1, size = 100, location = 0)
        }

        client?.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.body()?.error == false) {
                    _listStories.value = response.body()?.listStory
                    _isLoading.value = false
                    _isError.value = false
                } else {
                    _isError.value = true
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}
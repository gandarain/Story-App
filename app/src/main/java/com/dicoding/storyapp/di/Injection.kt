package com.dicoding.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.preference.LoginPreference

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storiesin")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = LoginPreference(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}
package com.dicoding.storyapp.ui.create_story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.model.CreateStoryResponse
import com.dicoding.storyapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postCreateStory(imageFile: File, desc: String, lat: Double, lon: Double): LiveData<Result<CreateStoryResponse>> {
        val file = reduceFileImage(imageFile as File)

        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        Log.d("Latitude", lat.toString())
        Log.d("Longitude", lon.toString())

        return storyRepository.createStory(imageMultipart, description, lat, lon)
    }
}
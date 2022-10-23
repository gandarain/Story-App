package com.dicoding.storyapp.ui.detail_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.storyapp.R
import com.dicoding.storyapp.constants.Constants
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.model.Story

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory = intent.getSerializableExtra(Constants.DETAIL_STORY) as? Story
        Log.d("DetailStoryActivity", detailStory.toString())
    }
}
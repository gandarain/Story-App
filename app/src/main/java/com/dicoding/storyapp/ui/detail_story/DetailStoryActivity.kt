package com.dicoding.storyapp.ui.detail_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.constants.Constants
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.utils.withDateFormat

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory = intent.getParcelableExtra<Story>(Constants.DETAIL_STORY) as Story

        setupToolBar()
        setupUi(detailStory)
    }

    private fun setupToolBar() {
        title = resources.getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun setupUi(detailStory: Story) {
        Glide.with(this@DetailStoryActivity)
            .load(detailStory.photoUrl)
            .fitCenter()
            .into(binding.storyImageView)

        detailStory.apply {
            binding.nameTextView.setText(name)
            binding.descriptionTextView.setText(description)
            binding.dateTextView.setText(createdAt.withDateFormat())
        }
    }
}
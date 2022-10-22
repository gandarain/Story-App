package com.dicoding.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.StoryLayoutBinding
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.utils.withDateFormat

class ListStoriesAdapter(private val listStories: List<Story>): RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {
    class ListViewHolder(binding: StoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var storyImageView: ImageView = binding.storyImageView
        var nameTextView: TextView = binding.nameTextView
        var dateTextView: TextView = binding.dateTextView
        var descriptionTextView: TextView = binding.descriptionTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        return ListViewHolder(
            StoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story: Story = listStories[position]
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .fitCenter()
            .into(holder.storyImageView)
        story.apply {
            holder.apply {
                nameTextView.text = name
                descriptionTextView.text = description
                dateTextView.text = createdAt.withDateFormat()
            }
        }
    }

    override fun getItemCount(): Int = listStories.size
}
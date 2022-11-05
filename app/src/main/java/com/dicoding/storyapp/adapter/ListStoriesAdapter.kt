package com.dicoding.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.constants.Constants
import com.dicoding.storyapp.databinding.StoryLayoutBinding
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.ui.detail_story.DetailStoryActivity
import com.dicoding.storyapp.utils.withDateFormat

class ListStoriesAdapter :
    PagingDataAdapter<Story, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

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
        val data = getItem(position)
        if (data != null) {
            holder.bind(story = data)
        }
    }

    class ListViewHolder(private val binding: StoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                nameTextView.text = story.name
                dateTextView.text = story.createdAt.withDateFormat()
                descriptionTextView.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .fitCenter()
                    .into(storyImageView)

                storyCardView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(Constants.DETAIL_STORY, story)
                    itemView.context.startActivity(
                        intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(binding.root.context as Activity).toBundle()
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
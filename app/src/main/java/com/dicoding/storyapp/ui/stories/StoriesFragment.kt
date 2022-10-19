package com.dicoding.storyapp.ui.stories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.custom_view.CustomAlertDialog
import com.dicoding.storyapp.model.Story
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.adapter.ListStoriesAdapter
import com.dicoding.storyapp.databinding.FragmentStoriesBinding

class StoriesFragment : Fragment() {

    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: StoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.listStories.observe(viewLifecycleOwner) {
            val isEmptyUser = it.isEmpty()
            if (isEmptyUser) {
                handlingEmptyUser(isEmptyUser)
            } else {
                showListStories(root.context, it)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            shodLoading(it)
        }

        homeViewModel.isError.observe(viewLifecycleOwner) {
            errorHandler(root.context, it)
        }

        return root
    }

    private fun shodLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.storiesRv.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.storiesRv.visibility = View.VISIBLE
        }
    }

    private fun errorHandler(context: Context, isError: Boolean) {
        if (isError) {
            CustomAlertDialog(context, R.string.error_message, R.drawable.error).show()
        }
    }

    private fun showListStories(context: Context, stories: List<Story>) {
        val storiesRv = binding.storiesRv

        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            storiesRv.layoutManager = GridLayoutManager(context, 2)
        } else {
            storiesRv.layoutManager = LinearLayoutManager(context)
        }

        val listStoriesAdapter = ListStoriesAdapter(stories)
        storiesRv.adapter = listStoriesAdapter
    }

    private fun handlingEmptyUser(isEmptyUser: Boolean) {
        if (isEmptyUser) {
            binding.emptyStories.emptyStoriesConstraintLayout.visibility = View.VISIBLE
        } else {
            binding.emptyStories.emptyStoriesConstraintLayout.visibility - View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
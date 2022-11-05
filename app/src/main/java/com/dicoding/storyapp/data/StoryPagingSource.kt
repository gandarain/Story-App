package com.dicoding.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.preference.LoginPreference

class StoryPagingSource(
    private val pref: LoginPreference,
    private val apiService: ApiService
) : PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = pref.getUser().token.toString()

            if (token.isNotEmpty()) {
                val responseData = token.let { apiService.getStories("Bearer $it", page, params.loadSize, 0) }
                if (responseData.isSuccessful) {
                    LoadResult.Page(
                        data = responseData.body()?.listStory ?: emptyList(),
                        prevKey = if (page == INITIAL_PAGE_INDEX) null else page -1,
                        nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else page + 1
                    )
                } else {
                    LoadResult.Error(Exception("Failed load story"))
                }
            } else {
                LoadResult.Error(Exception("Token empty"))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
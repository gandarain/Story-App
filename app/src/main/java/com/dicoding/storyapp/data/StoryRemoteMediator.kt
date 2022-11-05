package com.dicoding.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.preference.LoginPreference

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val pref: LoginPreference,
    private val apiService: ApiService
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        val token = pref.getUser().token.toString()

        try {
            val responseData = token.let { apiService.getStories(
                "Bearer $it",
                page,
                state.config.pageSize,
                0
            ) }

            return if (responseData.isSuccessful) {
                val endOfPaginationReached = responseData.body()!!.listStory.isEmpty()
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                MediatorResult.Error(Exception("Failed load story"))
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
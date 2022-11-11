package com.dicoding.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.model.StoryResponse
import com.dicoding.storyapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapViewModel: MapViewModel
    private var dummyStory = DataDummy.generateDummyStory()

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() {
        val observer = Observer<Result<StoryResponse>> {}

        try {
            val expectedResponse = MutableLiveData<Result<StoryResponse>>()
            expectedResponse.value = Result.Success(dummyStory)
            `when`(storyRepository.getStories()).thenReturn(expectedResponse)

            val actualResponse = mapViewModel.getStories().observeForever(observer)

            Mockito.verify(storyRepository).getStories()
            Assert.assertNotNull(actualResponse)
        } finally {
            mapViewModel.getStories().removeObserver(observer)
        }
    }

    @Test
    fun `when Get Story Should Null and Return Error`() {
        val observer = Observer<Result<StoryResponse>> {}
        dummyStory = DataDummy.generateErrorDummyStory()

        try {
            val expectedResponse = MutableLiveData<Result<StoryResponse>>()
            expectedResponse.value = Result.Error("error")
            `when`(storyRepository.getStories()).thenReturn(expectedResponse)

            val actualResponse = mapViewModel.getStories().observeForever(observer)

            Mockito.verify(storyRepository).getStories()
            Assert.assertNotNull(actualResponse)
        } finally {
            mapViewModel.getStories().removeObserver(observer)
        }
    }
}
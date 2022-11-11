package com.dicoding.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.model.RegisterResponse
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
class RegisterViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private var dummyRegisterResponse = DataDummy.generateDummyRegisterSuccess()

    private val dummyName = "name"
    private val dummyEmail = "email"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when Post Register Should Not Null and Return Success`() {
        val observer = Observer<Result<RegisterResponse>> {}
        try {
            val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
            expectedRegister.value = Result.Success(dummyRegisterResponse)
            `when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)

            val actualNews = registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).observeForever(observer)

            Mockito.verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
            Assert.assertNotNull(actualNews)
        } finally {
            registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).removeObserver(observer)
        }
    }

    @Test
    fun `when Post Register Should Null and Return Error`() {
        dummyRegisterResponse = DataDummy.generateDummyRegisterError()
        val observer = Observer<Result<RegisterResponse>> {}
        try {
            val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
            expectedRegister.value = Result.Success(dummyRegisterResponse)
            `when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)

            val actualNews = registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).observeForever(observer)

            Mockito.verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
            Assert.assertNotNull(actualNews)
        } finally {
            registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).removeObserver(observer)
        }
    }
}
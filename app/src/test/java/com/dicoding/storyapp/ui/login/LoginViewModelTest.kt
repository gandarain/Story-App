package com.dicoding.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.model.LoginResponse
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
class LoginViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private var dummyLoginResponse = DataDummy.generateDummyLoginSuccess()
    private val dummyEmail = "email"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Post Login Should Not Null and Return Success`() {
        val observer = Observer<Result<LoginResponse>> {}

        try {
            val expectedLogin = MutableLiveData<Result<LoginResponse>>()
            expectedLogin.value = Result.Success(dummyLoginResponse)
            `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

            val actualResponse = loginViewModel.postLogin(dummyEmail, dummyPassword).observeForever(observer)

            Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
            Assert.assertNotNull(actualResponse)
        } finally {
            loginViewModel.postLogin(dummyEmail, dummyPassword).removeObserver(observer)
        }
    }

    @Test
    fun `when Post Login Should Null and Return Error`() {
        dummyLoginResponse = DataDummy.generateDummyLoginError()
        val observer = Observer<Result<LoginResponse>> {}

        try {
            val expectedLogin = MutableLiveData<Result<LoginResponse>>()
            expectedLogin.value = Result.Error("invalid password")
            `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

            val actualResponse = loginViewModel.postLogin(dummyEmail, dummyPassword).observeForever(observer)

            Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
            Assert.assertNotNull(actualResponse)
        } finally {
            loginViewModel.postLogin(dummyEmail, dummyPassword).removeObserver(observer)
        }
    }
}
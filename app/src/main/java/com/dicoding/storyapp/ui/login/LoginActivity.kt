package com.dicoding.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.View
import androidx.activity.viewModels
import com.dicoding.storyapp.ui.main.MainActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.custom_view.CustomAlertDialog
import com.dicoding.storyapp.ui.register.RegisterActivity
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.model.LoginModel
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.preference.LoginPreference

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        hideActionBar()
        registerButtonHandler()
        loginButtonHandler()

        loginViewModel.isLoading.observe(this@LoginActivity) {
            showLoading(it)
        }

        loginViewModel.loginResponse.observe(this@LoginActivity) {
            loginHandler(it)
        }

        loginViewModel.isError.observe(this@LoginActivity) {
            errorHandler(it)
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun registerButtonHandler() {
        binding.loginLayout.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginButtonHandler() {
        binding.loginLayout.loginButton.setOnClickListener {
            val email = binding.loginLayout.emailEditText.text.toString()
            val password = binding.loginLayout.passwordEditText.text.toString()

            if (!isEmpty(email) && !isEmpty(password)) {
                loginViewModel.postLogin(email, password)
            } else {
                CustomAlertDialog(this, R.string.error_validation, R.drawable.error_form).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.loginLayout.root.visibility = View.GONE
        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.loginLayout.root.visibility = View.VISIBLE
        }
    }

    private fun loginHandler(loginResponse: LoginResponse) {
        if (!loginResponse.error) {
            saveLoginData(loginResponse)
            navigateToHome()
        }
    }

    private fun errorHandler(isError: Boolean) {
        if (isError) {
            CustomAlertDialog(this, R.string.error_message, R.drawable.error).show()
        }
    }

    private fun saveLoginData(loginResponse: LoginResponse) {
        val loginPreference = LoginPreference(this)
        val loginResult = loginResponse.loginResult
        val loginModel = LoginModel(
            name = loginResult.name, userId = loginResult.userId, token = loginResult.token
        )

        loginPreference.setLogin(loginModel)
    }

    private fun navigateToHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.loginLayout.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailTextView = ObjectAnimator.ofFloat(binding.loginLayout.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.loginLayout.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.loginLayout.emailEditText, View.ALPHA, 1f).setDuration(500)

        val passwordTextView = ObjectAnimator.ofFloat(binding.loginLayout.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.loginLayout.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.loginLayout.passwordEditText, View.ALPHA, 1f).setDuration(500)

        val loginButton = ObjectAnimator.ofFloat(binding.loginLayout.loginButton, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.loginLayout.registerButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(loginButton, registerButton)
        }

        AnimatorSet().apply {
            playSequentially(emailTextView, emailEditTextLayout, emailEditText, passwordTextView, passwordEditTextLayout, passwordEditText, together)
            start()
        }
    }
}
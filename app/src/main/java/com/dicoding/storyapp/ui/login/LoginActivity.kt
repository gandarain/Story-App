package com.dicoding.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.isEmpty
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.dicoding.storyapp.ui.main.MainActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.custom_view.CustomAlertDialog
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.ui.register.RegisterActivity
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.model.LoginModel
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.preference.LoginPreference
import com.dicoding.storyapp.utils.ViewModelFactory
import com.dicoding.storyapp.utils.isValidEmail
import com.dicoding.storyapp.utils.validateMinLength

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        playAnimation()
        hideActionBar()
        registerButtonHandler()
        loginButtonHandler()
        setMyButtonEnable()
        emailEditTextHandler()
        passwordEditTextHandler()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
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
                handlingLogin(email, password)
            } else {
                CustomAlertDialog(this, R.string.error_validation, R.drawable.error_form).show()
            }
        }
    }

    private fun handlingLogin(email: String, password: String) {
        loginViewModel.postLogin(email, password).observe(this@LoginActivity) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        errorHandler()
                    }
                    is Result.Success -> {
                        successLoginHandler(result.data)
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.loginLayout.root.visibility = View.GONE
        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.loginLayout.root.visibility = View.VISIBLE
        }
    }

    private fun successLoginHandler(loginResponse: LoginResponse) {
        saveLoginData(loginResponse)
        navigateToHome()
    }

    private fun errorHandler() {
        CustomAlertDialog(this, R.string.error_message, R.drawable.error).show()
    }

    private fun saveLoginData(loginResponse: LoginResponse) {
        val loginPreference = LoginPreference(this)
        val loginResult = loginResponse.loginResult
        val loginModel = LoginModel(
            name = loginResult?.name, userId = loginResult?.userId, token = loginResult?.token
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

    private fun setMyButtonEnable() {
        val emailEditText = binding.loginLayout.emailEditText.text
        val passwordEditText = binding.loginLayout.passwordEditText.text
        binding.loginLayout.loginButton.isEnabled =
            isValidEmail(emailEditText.toString()) && validateMinLength(passwordEditText.toString())
    }

    private fun emailEditTextHandler() {
        val emailEditText = binding.loginLayout.emailEditText
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun passwordEditTextHandler() {
        binding.loginLayout.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }
}
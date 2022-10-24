package com.dicoding.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.custom_view.CustomAlertDialog
import com.dicoding.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupToolbar()
        backButtonHandler()
        registerButtonHandler()

        registerViewModel.isLoading.observe(this@RegisterActivity) {
            showLoading(it)
        }

        registerViewModel.isSuccess.observe(this@RegisterActivity) {
            registerHandler(it)
        }

        registerViewModel.isError.observe(this@RegisterActivity) {
            errorHandler(it)
        }
    }

    private fun setupToolbar() {
        title = resources.getString(R.string.register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun backButtonHandler() {
        binding.registerLayout.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.registerLayout.root.visibility = View.GONE
        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.registerLayout.root.visibility = View.VISIBLE
        }
    }

    private fun errorHandler(isError: Boolean) {
        if (isError) {
            CustomAlertDialog(this, R.string.error_message, R.drawable.error).show()
        }
    }

    private fun registerHandler(isSuccess: Boolean) {
        if (isSuccess) {
            CustomAlertDialog(this, R.string.success_create_user, R.drawable.user_created).show()
            binding.registerLayout.emailEditText.text?.clear()
            binding.registerLayout.passwordEditText.text?.clear()
            binding.registerLayout.nameEditText.text?.clear()
            binding.registerLayout.confirmPasswordEditText.text?.clear()
        }
    }

    private fun registerButtonHandler() {
        binding.registerLayout.registerButton.setOnClickListener {
            val email = binding.registerLayout.emailEditText.text.toString()
            val password = binding.registerLayout.passwordEditText.text.toString()
            val name = binding.registerLayout.nameEditText.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
                registerViewModel.postRegister(name, email, password)
            } else {
                CustomAlertDialog(this, R.string.error_validation, R.drawable.error_form).show()
            }
        }
    }

    private fun playAnimation() {
        val emailTextView = ObjectAnimator.ofFloat(binding.registerLayout.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.registerLayout.emailEditText, View.ALPHA, 1f).setDuration(500)

        val nameTextView = ObjectAnimator.ofFloat(binding.registerLayout.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val nameEditText = ObjectAnimator.ofFloat(binding.registerLayout.nameEditText, View.ALPHA, 1f).setDuration(500)

        val passwordTextView = ObjectAnimator.ofFloat(binding.registerLayout.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.registerLayout.passwordEditText, View.ALPHA, 1f).setDuration(500)

        val confirmPasswordTextView = ObjectAnimator.ofFloat(binding.registerLayout.confirmPasswordTextView, View.ALPHA, 1f).setDuration(500)
        val confirmPasswordEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.confirmPasswordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val confirmPasswordEditText = ObjectAnimator.ofFloat(binding.registerLayout.confirmPasswordEditText, View.ALPHA, 1f).setDuration(500)

        val loginButton = ObjectAnimator.ofFloat(binding.registerLayout.registerButton, View.ALPHA, 1f).setDuration(500)
        val backButton = ObjectAnimator.ofFloat(binding.registerLayout.backButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(loginButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                emailTextView, emailEditTextLayout, emailEditText,
                nameTextView, nameEditTextLayout, nameEditText,
                passwordTextView, passwordEditTextLayout, passwordEditText,
                confirmPasswordTextView, confirmPasswordEditTextLayout, confirmPasswordEditText,
                together
            )
            start()
        }
    }
}
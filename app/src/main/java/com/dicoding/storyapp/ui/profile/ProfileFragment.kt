package com.dicoding.storyapp.ui.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.provider.Settings
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentProfileBinding
import com.dicoding.storyapp.model.LoginModel
import com.dicoding.storyapp.preference.LoginPreference
import com.dicoding.storyapp.preference.SettingPreferences
import com.dicoding.storyapp.ui.login.LoginActivity
import com.dicoding.storyapp.view_model.SettingViewModel
import com.dicoding.storyapp.view_model.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLoginPreference: LoginPreference
    private lateinit var loginModel: LoginModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mLoginPreference = LoginPreference(root.context)
        loginModel = mLoginPreference.getUser()

        playAnimation()
        setupUi()
        languageHandler()
        logoutHandler()
        getCurrentTheme(root.context)
        changeThemeHandler(root.context)

        return root
    }

    private fun setupUi() {
        binding.nameTextView.text = loginModel.name
        binding.userIdTextView.text = loginModel.userId
    }

    private fun languageHandler() {
        binding.languageCardView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun logoutHandler() {
        binding.logoutCardView.setOnClickListener {
            mLoginPreference.removeUser()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun getCurrentTheme(context: Context) {
        var themeTextView = binding.themeTextView
        val pref = context.let { SettingPreferences.getInstance(it.dataStore) }
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }

        }
    }

    private fun changeThemeHandler(context: Context) {
        val pref = context.let { SettingPreferences.getInstance(it.dataStore) }
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun playAnimation() {
        val userDetailCardView = ObjectAnimator.ofFloat(binding.userDetailCardView, View.ALPHA, 1f).setDuration(500)
        val settingTextView = ObjectAnimator.ofFloat(binding.settingTextView, View.ALPHA, 1f).setDuration(500)
        val themeCardView = ObjectAnimator.ofFloat(binding.themeCardView, View.ALPHA, 1f).setDuration(500)
        val languageCardView = ObjectAnimator.ofFloat(binding.languageCardView, View.ALPHA, 1f).setDuration(500)
        val logoutCardView = ObjectAnimator.ofFloat(binding.logoutCardView, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(userDetailCardView, settingTextView, themeCardView, languageCardView, logoutCardView)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
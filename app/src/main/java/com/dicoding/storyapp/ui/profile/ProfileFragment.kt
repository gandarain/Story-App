package com.dicoding.storyapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.databinding.FragmentDashboardBinding
import com.dicoding.storyapp.model.LoginModel
import com.dicoding.storyapp.preference.LoginPreference

class ProfileFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLoginPreference: LoginPreference
    private lateinit var loginModel: LoginModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mLoginPreference = LoginPreference(root.context)
        loginModel = mLoginPreference.getUser()

        setupUi()

        return root
    }

    private fun setupUi() {
        binding.nameTextView.text = loginModel.name
        binding.userIdTextView.text = loginModel.userId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
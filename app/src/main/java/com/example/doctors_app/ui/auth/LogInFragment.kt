package com.example.doctors_app.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.doctors_app.R
import com.example.doctors_app.databinding.FragmentLogInBinding
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentLogInBinding.inflate(layoutInflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        authViewModel.isLogin.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess -> {
                    binding.progressBar.isVisible=false
                    findNavController().navigate(R.id.action_logInFragment_to_toddyAppointmentsFragment)
                }
                it is StatusResult.OnLoading -> {
                    binding.progressBar.isVisible=true
                }
                it is StatusResult.OnError -> {
                    binding.progressBar.isVisible=false
                    toast(requireContext(),it.msg)
                }
            }
        })

        binding.loginBtnLogin.setOnClickListener {
            logIn()
        }
        binding.registerTvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
        }
        binding.forgetTvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_forgetPasswordFragment)
        }
    }

    private fun logIn()
    {
        val email=binding.emailEtLogin.text.toString().trim()
        val password=binding.passwordEtLogin.text.toString().trim()
        if (email.isEmpty())
        {
            binding.emailEtLogin.error="email require"
            binding.emailEtLogin.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailEtLogin.error="email invalid"
            binding.emailEtLogin.requestFocus()
            return
        }
        if (password.isEmpty())
        {
            binding.passwordEtLogin.error="password require"
            binding.passwordEtLogin.requestFocus()
            return
        }
        authViewModel.logIn(email,password)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}
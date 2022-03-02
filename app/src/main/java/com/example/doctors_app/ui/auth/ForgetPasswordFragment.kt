package com.example.doctors_app.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.doctors_app.R
import com.example.doctors_app.databinding.FragmentForgetPasswordBinding
import com.example.doctors_app.databinding.FragmentLogInBinding
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    private var _binding: FragmentForgetPasswordBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentForgetPasswordBinding.inflate(layoutInflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.backBtnForgetpass.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.sendPassBtnForgetpass.setOnClickListener {
            val email=binding.emailEtForgetpass.text.toString()
            authViewModel.forgetPassWord(email)
        }
        authViewModel.isPassSent.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess ->{
                    binding.progressForget.isVisible=false
                    Snackbar.make(binding.forgetScreen,"Password send successfully",Snackbar.LENGTH_SHORT)
                        .setAction("ok",{}).show()
                }
                it is StatusResult.OnError ->{
                    binding.progressForget.isVisible=false
                }
                it is StatusResult.OnLoading ->{
                    binding.progressForget.isVisible=true
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}
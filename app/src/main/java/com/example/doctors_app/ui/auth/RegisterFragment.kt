package com.example.doctors_app.ui.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.doctors_app.R
import com.example.doctors_app.databinding.FragmentRegister2Binding
import com.example.doctors_app.databinding.FragmentRegisterBinding
import com.example.doctors_app.models.Doctors
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val navArgs by navArgs<RegisterFragmentArgs>()

    private var mspeiality:String?=null
    private var gender:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentRegisterBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        mspeiality=navArgs.spitialistName
        mspeiality.let { binding.spetialityEtRegister.setText(it) }
        setupView()
        authViewModel.isRegister.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnLoading ->{
                    binding.pbRegister.isVisible=true
                }
                it is StatusResult.OnSuccess ->{
                    binding.pbRegister.isVisible=false
                    findNavController().navigate(R.id.action_registerFragment_to_register2Fragment)
                }
                it is StatusResult.OnError ->{
                    binding.pbRegister.isVisible=false
                    toast(requireActivity(),it.msg)
                    Log.d("mylog",it.msg!!.toString())
                }
            }
        })

    }

    private fun setupView() {
        initialTitleItemsDropMenu()

        binding.spetialityEtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_cheakSpecialistFragment)
        }
        binding.registerBtnRegister.setOnClickListener {
            register()
        }

    }

    private fun register()
    {
        val titileEn=binding.titleEnEtRegister.text.toString()
        val fullNameEn=binding.fullNameEnEtRegister.text.toString()
        val phoneNumber=binding.phoneEtRegister.text.toString()
        val email=binding.emailEtRegister.text.toString()
        val password=binding.passwordEtRegister.text.toString()
        val confirmPass=binding.passwordConfirmEtRegister.text.toString()
        val speiality=mspeiality
        val termsCheak=binding.termsChRegister.isChecked
        when(binding.genderRgRegister.checkedRadioButtonId)
        {
            R.id.famale_rb_register ->{ gender="female" }
            R.id.male_rb_register ->{ gender="male" }
        }
        if(titileEn.isEmpty())
        {
            binding.titleEnEtRegister.error="this feild required"
            binding.titleEnEtRegister.requestFocus()
            return
        }
        if(fullNameEn.isEmpty())
        {
            binding.fullNameEnEtRegister.error="this feild required"
            binding.fullNameEnEtRegister.requestFocus()
            return
        }
        if(gender.isNullOrEmpty())
        {
            toast(requireActivity(),"please select gender")
            return
        }
        if(phoneNumber.isEmpty())
        {
            binding.phoneEtRegister.error="this feild required"
            binding.phoneEtRegister.requestFocus()
            return
        }
        if(email.isEmpty())
        {
            binding.emailEtRegister.error="this feild required"
            binding.emailEtRegister.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailEtRegister.error="email not valid"
            binding.emailEtRegister.requestFocus()
            return
        }
        if(password.isEmpty())
        {
            binding.passwordEtRegister.error="this feild required"
            binding.passwordEtRegister.requestFocus()
            return
        }
        if(password.length < 6)
        {
            binding.passwordEtRegister.error="password weak"
            binding.passwordEtRegister.requestFocus()
            return
        }
        if(confirmPass.isEmpty())
        {
            binding.passwordConfirmEtRegister.error="this feild required"
            binding.passwordConfirmEtRegister.requestFocus()
            return
        }
        if(confirmPass!=password)
        {
            binding.passwordEtRegister.error="try agin not confirmed"
            binding.passwordEtRegister.requestFocus()
            return
        }
        if(speiality.isNullOrEmpty())
        {
            binding.passwordEtRegister.error="please select spetialtiest"
            binding.passwordEtRegister.requestFocus()
            return
        }
        if(!termsCheak)
        {
            toast(requireActivity(),"you sholud to accept terms")
            return
        }
        val doctros=Doctors(
            nameEN = fullNameEn,
            spicialty = speiality,
            email = email,
            title = titileEn,
            gender = gender,
            phone = phoneNumber,
        )
        authViewModel.register(email,password,doctros)
    }

    private fun initialTitleItemsDropMenu() {
        val titles= arrayOf("Assistant Lecture","Associate Professor","Consultant","Lecture","Professor DR","Specialist")
        val adapter=ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,titles)
        binding.titleEnEtRegister.setAdapter(adapter)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
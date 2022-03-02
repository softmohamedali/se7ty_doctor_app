package com.example.doctors_app.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.R
import com.example.doctors_app.adapters.SpetialitestItemAdapter
import com.example.doctors_app.databinding.FragmentCheakSpecialistBinding
import com.example.doctors_app.databinding.FragmentLogInBinding
import com.example.doctors_app.models.Specialties
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheakSpecialistFragment : Fragment(),SpetialitestItemAdapter.SpetialtiesItemClick {
    private var _binding: FragmentCheakSpecialistBinding?=null
    private val binding get() = _binding!!
    private val spetialitestItemAdapter by lazy { SpetialitestItemAdapter(this) }
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCheakSpecialistBinding.inflate(layoutInflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        setUpRecy()
        binding.backBtnSpeiatest.setOnClickListener {
            findNavController().popBackStack()
        }
        authViewModel.getSearchSpecialties(" ")
        binding.searchEtSpeiatest.doOnTextChanged { text, start, before, count ->
            authViewModel.getSearchSpecialties(text.toString())
        }
        authViewModel.speialitest.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess ->{
                    binding.progressSp.isVisible=false
                    toast(requireActivity(),"${it.data?.size} sucss")
                    spetialitestItemAdapter.setData(it.data!!)
                }
                it is StatusResult.OnError ->{
                    binding.progressSp.isVisible=false
                    toast(requireActivity(),"${it.msg} error")
                }
                it is StatusResult.OnLoading ->{
                    binding.progressSp.isVisible=true
                    toast(requireActivity(),"${it.data?.size} loading")
                }
            }
        })
    }

    fun setUpRecy()
    {
        binding.recySpeiatest.apply {
            adapter=spetialitestItemAdapter
            layoutManager=LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun itemClick(name: String) {
        val action=CheakSpecialistFragmentDirections.actionCheakSpecialistFragmentToRegisterFragment(name)
        findNavController().navigate(action)
    }

}
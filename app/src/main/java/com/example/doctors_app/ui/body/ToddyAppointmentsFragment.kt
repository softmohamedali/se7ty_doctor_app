package com.example.doctors_app.ui.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.R
import com.example.doctors_app.adapters.MyAppointementItemadapter
import com.example.doctors_app.databinding.ActivityMainBinding
import com.example.doctors_app.databinding.FragmentToddyAppointmentsBinding
import com.example.doctors_app.models.Client
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.viewmodel.AuthViewModel
import com.example.doctors_app.viewmodel.MyAppointementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class ToddyAppointmentsFragment : Fragment(),MyAppointementItemadapter.MyAppointementItemClick {
    private var _binding: FragmentToddyAppointmentsBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val myAppointementViewModel by viewModels<MyAppointementViewModel>()
    private val myAppointemntItemAdapter by lazy { MyAppointementItemadapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentToddyAppointmentsBinding.inflate(layoutInflater)
        setupView()

        return binding.root
    }

    private fun setupView() {
        if (authViewModel.user ==null)
        {
            findNavController().navigate(R.id.action_toddyAppointmentsFragment_to_logInFragment)
        }

        setUpRecy()
        setUpObservers()
        myAppointementViewModel.getTodayAppointement()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            myAppointementViewModel.todayAppointment.collect {
                when{
                    it is StatusResult.OnLoading ->{
                        binding.tvError.isVisible=true
                    }
                    it is StatusResult.OnError ->{
                        binding.tvError.text=it.msg
                        binding.tvError.isVisible=true
                    }
                    it is StatusResult.OnSuccess ->{
                        myAppointemntItemAdapter.setData(it.data!!)
                        binding.tvError.isVisible=false
                    }
                }
            }
        }
    }

    fun setUpRecy()
    {
        binding.recyTodayAppointement.apply {
            adapter=myAppointemntItemAdapter
            layoutManager= LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun viewEmrClick(client: Client) {
        val action=ToddyAppointmentsFragmentDirections
            .actionToddyAppointmentsFragmentToViewClientEMRFragment(client)
        findNavController().navigate(action)
    }

    override fun startChatClick(client: String) {

    }


}
package com.example.doctors_app.ui.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.adapters.MyAppointementItemadapter
import com.example.doctors_app.databinding.FragmentMyApponitmentBinding
import com.example.doctors_app.models.Client
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.viewmodel.AuthViewModel
import com.example.doctors_app.viewmodel.MyAppointementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MyApponitmentFragment : Fragment() ,MyAppointementItemadapter.MyAppointementItemClick{
    private var _binding: FragmentMyApponitmentBinding?=null
    private val binding get() = _binding!!
    private val myAppointemntItemAdapter by lazy { MyAppointementItemadapter(this) }
    private val myAppointementViewModel by viewModels<MyAppointementViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMyApponitmentBinding.inflate(layoutInflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        setUpRecy()
        setUpObservers()
        myAppointementViewModel.getMyAppointment()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            myAppointementViewModel.myAppointment.collect {
                when{
                    it is StatusResult.OnLoading ->{
                        binding.textView21.isVisible=true
                    }
                    it is StatusResult.OnError ->{
                        binding.textView21.text=it.msg
                        binding.textView21.isVisible=true
                    }
                    it is StatusResult.OnSuccess ->{
                        myAppointemntItemAdapter.setData(it.data!!)
                        binding.textView21.isVisible=false
                    }
                }
            }
        }
    }

    fun setUpRecy()
    {
        binding.recyMyappointement.apply {
            adapter=myAppointemntItemAdapter
            layoutManager= LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun viewEmrClick(client: Client) {
        val action=MyApponitmentFragmentDirections
            .actionMyApponitmentFragmentToViewClientEMRFragment(client)
        findNavController().navigate(action)
    }

    override fun startChatClick(client: String) {

    }
}
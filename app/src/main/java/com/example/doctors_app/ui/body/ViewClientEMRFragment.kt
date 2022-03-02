package com.example.doctors_app.ui.body

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctors_app.R
import com.example.doctors_app.adapters.MedicalTestItemAdapter
import com.example.doctors_app.adapters.MedicationItemAdapter
import com.example.doctors_app.databinding.FragmentToddyAppointmentsBinding
import com.example.doctors_app.databinding.FragmentViewClientEMRBinding
import com.example.doctors_app.models.Client
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import com.example.doctors_app.viewmodel.MyAppointementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ViewClientEMRFragment : Fragment() ,MedicalTestItemAdapter.MedicalTestItemClick{
    private var _binding: FragmentViewClientEMRBinding?=null
    private val binding get() = _binding!!
    private val myAppViewModel by viewModels<MyAppointementViewModel>()
    private val navArgs by navArgs<ViewClientEMRFragmentArgs>()
    private val medicationAdapter by lazy { MedicationItemAdapter() }
    private val medicalTestAdapter by lazy { MedicalTestItemAdapter(this) }
    private lateinit var mClient:Client
    private var isMedicalTestShow=false
    private var isMedicationShow=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentViewClientEMRBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        mClient=navArgs.client
        Log.d("mylog","hhhhhhhh${mClient.clientId}")
        setUpView()
        myAppViewModel.getUserMedicalTest(mClient.clientId!!)
        myAppViewModel.getUserMedication(mClient.clientId!!)
        toast(requireContext(),"${mClient.clientId!!}")
        setUpObservers()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            myAppViewModel.medicalTestUser.collect {
                when{
                    it is StatusResult.OnSuccess->{
                        medicalTestAdapter.setData(it.data!!)
                        toast(requireContext(),"${it.data!!.size}")
                    }
                    it is StatusResult.OnError->{
                        toast(requireContext(),"${it.msg}")
                    }
                    it is StatusResult.OnLoading->{

                    }
                }
            }

        }
        lifecycleScope.launchWhenStarted {
            myAppViewModel.medicationUser.collect{
                when{
                    it is StatusResult.OnSuccess->{
                        medicationAdapter.setData(it.data!!)
                    }
                    it is StatusResult.OnError->{
                        toast(requireContext(),"${it.msg}")
                    }
                    it is StatusResult.OnLoading->{

                    }
                }
            }
        }
    }

    private fun setUpView() {
        binding.btnBackUserfrag.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvName.text="Client Name: ${mClient.name}"
        binding.tvAge.text="Client Age: ${mClient.age}"
        binding.tvCientPhone.text="Client Phone: ${mClient.phone}"
        binding.tvDeaseses.text="Deseasses Suffring from: ${mClient.desease}"
        binding.tvEmail.text="Email For Contact: ${mClient.emailToContact}"
        binding.tvGender.text="Client Gender: ${mClient.gender}"
        setUpRecyclers()
        binding.btnShowMedicaltest.setOnClickListener {
            if (isMedicalTestShow)
            {
                showMedicalTestRecy(false)
                isMedicalTestShow=false
            }else{
                isMedicalTestShow=true
                showMedicalTestRecy(true)
            }
        }
        binding.btnShowMedication.setOnClickListener {
            if (isMedicationShow)
            {
                showMedicationRecy(false)
                isMedicationShow=false
            }else{
                isMedicationShow=true
                showMedicationRecy(true)
            }
        }
    }

    private fun setUpRecyclers() {
        binding.recyMedicalTest.apply {
            adapter=medicalTestAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        binding.recyMedication.apply {
            adapter=medicationAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun showMedicalTestRecy(show:Boolean){
        if (show)
        {
            binding.recyMedicalTest.visibility=View.VISIBLE
            binding.imgArrowMedicaltest.rotationX=180f
        }else{
            binding.recyMedicalTest.visibility=View.GONE
            binding.imgArrowMedicaltest.rotationX=0f
        }
    }
    private fun showMedicationRecy(show:Boolean){
        if (show)
        {
            binding.recyMedication.visibility=View.VISIBLE
            binding.imgArrowMedication.rotationX=180f
        }else{
            binding.recyMedication.visibility=View.GONE
            binding.imgArrowMedication.rotationX=0f
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun btnViewAttachmentClick(img: String) {

    }

}
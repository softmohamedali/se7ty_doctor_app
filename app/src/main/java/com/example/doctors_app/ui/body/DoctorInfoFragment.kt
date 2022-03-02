package com.example.doctors_app.ui.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.doctors_app.R
import com.example.doctors_app.databinding.FragmentDoctorInfoBinding
import com.example.doctors_app.databinding.FragmentMyApponitmentBinding
import com.example.doctors_app.helpers.Validation
import com.example.doctors_app.models.Doctors
import com.example.doctors_app.utils.MyUtils
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorInfoFragment : Fragment() {
    private var _binding: FragmentDoctorInfoBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var mdoctors:Doctors
    private var lati:String?=null
    private var long:String?=null
    private val navArgs by navArgs<DoctorInfoFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDoctorInfoBinding.inflate(layoutInflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        lati=navArgs.lati
        long=navArgs.long
        if (!lati?.trim().isNullOrEmpty()&&!long?.trim().isNullOrEmpty())
        {
            binding.locationEtDoctorinfo.setText("${lati},${long}")
        }
        authViewModel.getUserInfo()
        authViewModel.userInfo.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess ->{
                    mdoctors=it.data!!
                    setDoctorDate()
                    binding.pbDoctorIngo.isVisible=false
                }
                it is StatusResult.OnLoading ->{
                    binding.pbDoctorIngo.isVisible=true
                }
                it is StatusResult.OnError ->{
                    toast(requireActivity(),"${it.msg}")
                    binding.pbDoctorIngo.isVisible=false
                }
            }
        })
        authViewModel.isRegister.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess ->{
                    binding.pbDoctorIngo.isVisible=false
                }
                it is StatusResult.OnLoading ->{
                    binding.pbDoctorIngo.isVisible=true
                }
                it is StatusResult.OnError ->{
                    toast(requireActivity(),"${it.msg}")
                    binding.pbDoctorIngo.isVisible=false
                }
            }
        })
        binding.saveBtnDoctorinfo.setOnClickListener {
            saveDoctor()
        }
    }

    private fun saveDoctor() {
        if (
            Validation.isFeildIsEmpty(binding.fullNameEnEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.phoneEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.emailEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.namrArEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.descriptionEnEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.discriptionArDoctorinfo)||
            Validation.isFeildIsEmpty(binding.locationEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.pricefeesEtDoctorinfo)||
            Validation.isFeildIsEmpty(binding.pricefollowEtDoctorinfo)
        ){
            return
        }
        val nameEn=binding.fullNameEnEtDoctorinfo.text.toString()
        val phone=binding.phoneEtDoctorinfo.text.toString()
        val email=binding.emailEtDoctorinfo.text.toString()
        val nameAr=binding.namrArEtDoctorinfo.text.toString()
        val descrEn=binding.descriptionEnEtDoctorinfo.text.toString()
        val descrAr=binding.discriptionArDoctorinfo.text.toString()
        val loaction=binding.locationEtDoctorinfo.text.toString()
        val priceFees=binding.pricefeesEtDoctorinfo.text.toString()
        val priceFollow=binding.pricefollowEtDoctorinfo.text.toString()
        val doctor=mdoctors
        doctor.nameAr=nameAr
        doctor.nameEN=nameEn
        doctor.phone=phone
        doctor.email=email
        doctor.descriptionAR=descrAr
        doctor.descriptionEn=descrEn
        doctor.loc=loaction
        doctor.priceFees=priceFees
        doctor.priceFollowUp=priceFollow
        authViewModel.saveDoctorsInfo(doctor,null,null)
    }

    private fun setDoctorDate() {
        binding.imgDoctorinfo.load(mdoctors.photo)
        binding.fullNameEnEtDoctorinfo.setText(mdoctors.nameEN)
        binding.phoneEtDoctorinfo.setText(mdoctors.phone)
        binding.emailEtDoctorinfo.setText(mdoctors.email)
        binding.namrArEtDoctorinfo.setText(mdoctors.nameAr)
        binding.descriptionEnEtDoctorinfo.setText(mdoctors.descriptionEn)
        binding.discriptionArDoctorinfo.setText(mdoctors.descriptionAR)
        binding.locationEtDoctorinfo.setText(mdoctors.loc)
        binding.pricefeesEtDoctorinfo.setText(mdoctors.priceFees)
        binding.pricefollowEtDoctorinfo.setText(mdoctors.priceFollowUp)
        if (!lati?.trim().isNullOrEmpty()&&!long?.trim().isNullOrEmpty())
        {
            binding.locationEtDoctorinfo.setText("${lati},${long}")
        }
        binding.pickPositionBtnDoctorinfo.setOnClickListener {
            findNavController().navigate(R.id.action_doctorInfoFragment_to_pickDoctorPositionFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}
package com.example.doctors_app.ui.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.doctors_app.R
import com.example.doctors_app.databinding.FragmentRegister2Binding
import com.example.doctors_app.models.Doctors
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class Register2Fragment : Fragment() {
    private var _binding: FragmentRegister2Binding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private var personalimgByteArray:ByteArray?=null
    private var medicalimgByteArray:ByteArray?=null
    private var mDoctors:Doctors?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentRegister2Binding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp()
    {
        setUpView()
        binding.imgRegister2.setOnClickListener {
            selectImg()
        }
        binding.registerBtnRegister2.setOnClickListener {
            register2()
        }
        binding.addMedicalPhotoBtnRegister2.setOnClickListener {
            selectImg2()
        }
        authViewModel.getUserInfo()
        authViewModel.userInfo.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess ->{
                    mDoctors=it.data
                    binding.pbRegister2.isVisible=false
                }
                it is StatusResult.OnLoading ->{
                    binding.pbRegister2.isVisible=true
                }
                it is StatusResult.OnError ->{
                    binding.pbRegister2.isVisible=false
                    toast(requireActivity(),"${it.msg}")
                }
            }
        })
        authViewModel.isRegister.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnLoading ->{
                    binding.pbRegister2.isVisible=true
                    binding.view2.isVisible=true
                }
                it is StatusResult.OnSuccess ->{
                    binding.view2.isVisible=true
                    binding.pbRegister2.isVisible=false
                    val mytoast=Toast(requireActivity())
                    mytoast.apply {
                        duration=Toast.LENGTH_SHORT
                        view=LayoutInflater.from(requireActivity()).inflate(R.layout.layout_success_create,null,false)
                        setGravity(Gravity.CENTER,0,0)
                    }
                    mytoast.show()
                    findNavController().navigate(R.id.action_register2Fragment_to_logInFragment)
                }
                it is StatusResult.OnError ->{
                    binding.pbRegister2.isVisible=false
                    binding.view2.isVisible=false
                    toast(requireActivity(),it.msg)
                    Log.d("mylog",it.msg!!.toString())
                }
            }
        })

    }

    private fun setUpView()
    {
        val lang= listOf<String>("english","arabic")
        val country= listOf("egypt","UAE","soudia arabia","syria")
        val city= listOf("cairo","menofia")
        val langadapter= ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,lang)
        val countryadapter=ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,country)
        val cityadapter=ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,city)
        binding.spokenLangugeEtRegister2.setAdapter(langadapter)
        binding.countryEtRegister2.setAdapter(countryadapter)
        binding.cityEtRegister2.setAdapter(cityadapter)

    }

    private fun register2() {
        val nameAr=binding.namrArEtRegister2.text.toString()
        val dateOfBridth=binding.dateBridthEtRegister2.text.toString()
        val desEn=binding.descriptionEnEtRegister2.text.toString()
        val desAr=binding.discriptionArEtRegister2.text.toString()
        val medicalIdNum=binding.medicalIdNumberEtRegister2.text.toString()
        val country=binding.countryEtRegister2.text.toString()
        val city=binding.cityEtRegister2.text.toString()
        val spokenLanguge=binding.spokenLangugeEtRegister2.text.toString()

        if(nameAr.isEmpty())
        {
            binding.namrArEtRegister2.error="this feild required"
            binding.namrArEtRegister2.requestFocus()
            return
        }
        if(dateOfBridth.isEmpty())
        {
            binding.dateBridthEtRegister2.error="this feild required"
            binding.dateBridthEtRegister2.requestFocus()
            return
        }
        if(desEn.isEmpty())
        {
            binding.descriptionEnEtRegister2.error="this feild required"
            binding.descriptionEnEtRegister2.requestFocus()
            return
        }
        if(desAr.isEmpty())
        {
            binding.discriptionArEtRegister2.error="this feild required"
            binding.discriptionArEtRegister2.requestFocus()
            return
        }
        if(medicalIdNum.isEmpty())
        {
            binding.medicalIdNumberEtRegister2.error="this feild required"
            binding.medicalIdNumberEtRegister2.requestFocus()
            return
        }
        if(medicalimgByteArray==null)
        {
            binding.medicalIdPhotoEtRegister2.error="this feild required"
            binding.medicalIdPhotoEtRegister2.requestFocus()
            return
        }
        if(country.isEmpty())
        {
            binding.countryEtRegister2.error="this feild required"
            binding.countryEtRegister2.requestFocus()
            return
        }
        if(city.isEmpty())
        {
            binding.cityEtRegister2.error="this feild required"
            binding.cityEtRegister2.requestFocus()
            return
        }
        if(spokenLanguge.isEmpty())
        {
            binding.spokenLangugeEtRegister2.error="this feild required"
            binding.spokenLangugeEtRegister2.requestFocus()
            return
        }
        if (personalimgByteArray==null)
        {
            toast(requireActivity(),"please select photo")
            return
        }
        if (mDoctors==null)
        {
            toast(requireActivity(),"please try Again with internt connection")
            return
        }
        if (mDoctors!=null)
        {
            mDoctors?.descriptionEn=desEn
            mDoctors?.descriptionAR=desAr
            mDoctors?.dataOfridth=dateOfBridth
            mDoctors?.nameAr=nameAr
            mDoctors?.medicalId=medicalIdNum
            mDoctors?.country=country
            mDoctors?.city=city
            mDoctors?.spokenLang=spokenLanguge

        }
        authViewModel.saveDoctorsInfo(mDoctors!!,personalimgByteArray!!,medicalimgByteArray!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun selectImg(){
        val intent=Intent().apply {
            action=Intent.ACTION_GET_CONTENT
            type="image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/png", "image/jpeg"))
        }
        resultLauncher.launch(Intent.createChooser(intent, "chose img"))
    }

    private fun selectImg2(){
        val intent=Intent().apply {
            action=Intent.ACTION_GET_CONTENT
            type="image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/png", "image/jpeg"))
        }
        resultLauncher2.launch(Intent.createChooser(intent, "chose img"))
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK&&result.data!=null) {
            val imgUri= result.data!!.data
            val bitmap= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
            val outByteArray= ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 25, outByteArray)
            val imgByteArr=outByteArray.toByteArray()
            personalimgByteArray=imgByteArr
            binding.imgRegister2.setImageURI(imgUri)
        }
    }

    var resultLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK&&result.data!=null) {
            val imgUri= result.data!!.data
            val bitmap= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
            val outByteArray= ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 25, outByteArray)
            val imgByteArr=outByteArray.toByteArray()
            medicalimgByteArray=imgByteArr
            binding.medicalIdPhotoEtRegister2.setText(imgUri.toString())
        }
    }


}
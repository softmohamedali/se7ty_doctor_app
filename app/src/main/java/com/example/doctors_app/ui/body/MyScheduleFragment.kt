package com.example.doctors_app.ui.body

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.R
import com.example.doctors_app.adapters.DateScheduleAdapter
import com.example.doctors_app.databinding.FragmentMyScheduleBinding
import com.example.doctors_app.models.Client
import com.example.doctors_app.models.TimeSchedule
import com.example.doctors_app.utils.StatusResult
import com.example.doctors_app.utils.toast
import com.example.doctors_app.viewmodel.AuthViewModel
import com.example.doctors_app.viewmodel.MySchedulaViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MyScheduleFragment : Fragment(),DateScheduleAdapter.DataSchedulementItemClick{
    private var _binding: FragmentMyScheduleBinding?=null
    private val binding get() = _binding!!
    private val mySchedulaViewModel by viewModels<MySchedulaViewModel>()
    private val dateSchedulaAdapter by lazy { DateScheduleAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentMyScheduleBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        setUpView()
        setUpObservers()
    }

    private fun setUpObservers() {
        mySchedulaViewModel.getAllDataSchedula()
        mySchedulaViewModel.isDataSchedulaAdded.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess -> {
                    binding.probMySchedulaGraf.isVisible=false
                    toast(requireContext(),"sucesss")
                }
                it is StatusResult.OnLoading -> {
                    binding.probMySchedulaGraf.isVisible=true
                    toast(requireContext(),"loading")
                }
                it is StatusResult.OnError -> {
                    binding.probMySchedulaGraf.isVisible=false
                    toast(requireContext(),it.msg)
                }
            }
        })
        mySchedulaViewModel.dataSchedul.observe(viewLifecycleOwner,{
            when{
                it is StatusResult.OnSuccess -> {
                    binding.progrRecyMySchedulaGraf.isVisible=false
                    dateSchedulaAdapter.setData(it.data!!)
                }
                it is StatusResult.OnLoading -> {
                    binding.progrRecyMySchedulaGraf.isVisible=true
                }
                it is StatusResult.OnError -> {
                    dateSchedulaAdapter.setData(mutableListOf())
                    binding.progrRecyMySchedulaGraf.isVisible=false
                    toast(requireContext(),it.msg)
                }
            }
        })
    }

    private fun setUpView() {
        setUpRecy()
        binding.pickTimeFromEt.setOnClickListener {
            showfromTimePiker()
        }
        binding.pickTimeToEt.setOnClickListener {
            showToTimePiker()
        }
        binding.pickDateEt.setOnClickListener {
            showDateDialog()
        }
        binding.addTimeBtn.setOnClickListener {
            addTimeSchedula()
        }

    }

    private fun addTimeSchedula()
    {
        val from=binding.pickTimeFromEt.text.toString()
        val to=binding.pickTimeToEt.text.toString()
        val date=binding.pickDateEt.text.toString()
        if (from.isEmpty())
        {
            binding.pickTimeFromEt.error="please select time"
            binding.pickTimeFromEt.requestFocus()
            return
        }
        if (to.isEmpty())
        {
            binding.pickTimeToEt.error="please select time"
            binding.pickTimeToEt.requestFocus()
            return
        }
        if (date.isEmpty())
        {
            binding.pickDateEt.error="please select time"
            binding.pickDateEt.requestFocus()
            return
        }
        mySchedulaViewModel.addTimeSchedula(TimeSchedule(
            toTime = to,
            fromTime = from,
            date =date
        ))
    }

    private fun setUpRecy(){
        binding.recyDataSchedulaFrag.apply {
            adapter=dateSchedulaAdapter
            layoutManager=LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false)
        }
    }

    private fun showToTimePiker() {
        val calender = Calendar.getInstance()
        val TimePiker = TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
            calender.set(Calendar.HOUR, h)
            calender.set(Calendar.MINUTE, m)
            val format = "HH:mm"
            val simpleDateFormat = SimpleDateFormat(format, Locale.UK)
            binding.pickTimeToEt.setText(simpleDateFormat.format(calender.time))
        }
        TimePickerDialog(
            requireActivity(),
            TimePiker,
            calender.get(Calendar.HOUR),
            calender.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(activity)
        ).show()
    }

    private fun showDateDialog() {

        val calender = Calendar.getInstance()
        val datePiker=DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
             calender.set(Calendar.YEAR,y)
             calender.set(Calendar.MONTH,m)
             calender.set(Calendar.DAY_OF_MONTH,d)
            val format="dd-MM-yyyy"
            val simpleDateFormat=SimpleDateFormat(format,Locale.UK)
            binding.pickDateEt.setText(simpleDateFormat.format(calender.time))
        }
        DatePickerDialog(requireActivity(),datePiker,calender.get(Calendar.YEAR)
        ,calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showfromTimePiker() {
        val calender = Calendar.getInstance()
        val TimePiker = TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
            calender.set(Calendar.HOUR, h)
            calender.set(Calendar.MINUTE, m)
            val format = "HH:mm"
            val simpleDateFormat = SimpleDateFormat(format, Locale.UK)
            binding.pickTimeFromEt.setText(simpleDateFormat.format(calender.time))
        }
        TimePickerDialog(
            requireActivity(),
            TimePiker,
            calender.get(Calendar.HOUR),
            calender.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(activity)
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun deleteItemClick(dateSchedula: TimeSchedule) {
        mySchedulaViewModel.delteDateSchedula(dateSchedula)
    }


}
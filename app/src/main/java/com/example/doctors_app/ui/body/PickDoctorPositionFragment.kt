package com.example.doctors_app.ui.body

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.doctors_app.R
import com.example.doctors_app.databinding.FragmentMyApponitmentBinding
import com.example.doctors_app.databinding.FragmentPickDoctorPositionBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PickDoctorPositionFragment : Fragment() {
    private var _binding: FragmentPickDoctorPositionBinding?=null
    private val binding get() = _binding!!
    private lateinit var lati:String
    private lateinit var lang:String
    private val callback = OnMapReadyCallback { googleMap ->
        val cairo = LatLng(30.04447670707573, 31.235290669369935)
        lati="30.04447670707573"
        lang="31.235290669369935"
        var marker=googleMap.addMarker(MarkerOptions().position(cairo))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cairo))
        googleMap.uiSettings.apply {
            isZoomControlsEnabled=true
        }

        googleMap.setOnMapClickListener {
            marker?.remove()
            marker=googleMap.addMarker(MarkerOptions().position(it))
            lati=it.latitude.toString()
            lang=it.longitude.toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPickDoctorPositionBinding.inflate(layoutInflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.btnBackPickposit.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.fabConfirmPickPos.setOnClickListener {
            val action=PickDoctorPositionFragmentDirections.actionPickDoctorPositionFragmentToDoctorInfoFragment(
                long = lang,
                lati = lati
            )
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
package com.example.doctors_app

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.doctors_app.databinding.ActivityMainBinding
import com.example.doctors_app.utils.Constants
import com.example.doctors_app.utils.LocationUtility
import com.example.doctors_app.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController:NavController

    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()


    }

    private fun setUp() {
        setUpNavigationComponent()
        setUpNavDrawer()
    }

    private fun setUpNavDrawer() {
        val actionToogle=ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.open,
            R.string.close
        )
        binding.drawer.addDrawerListener(actionToogle)
        binding.menuImg.setOnClickListener {
            binding.drawer.open()
        }
        binding.navView.bringToFront()
        binding.navView.requestLayout()
        binding.navView.setNavigationItemSelectedListener{
             when(it.itemId)
             {
                 R.id.logout_nav_view -> {
                     authViewModel.logOut()
                     navController.navigate(R.id.glopal_to_logInFragment)
                     closeNavDraw()
                 }
                 else ->{

                 }
             }
            true
        }
    }

    private fun setUpNavigationComponent() {
        val hostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=hostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        destinationChangedListener()
    }

    fun destinationChangedListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id)
            {
                R.id.toddyAppointmentsFragment->{
                    binding.tvTitle.text="tody Appointments"
                    binding.bottomNavigationView.visibility=View.VISIBLE
                    binding.constraintLayout.visibility=View.VISIBLE
                }
                R.id.doctorInfoFragment->{
                    binding.tvTitle.text="My Info"
                    binding.bottomNavigationView.visibility=View.VISIBLE
                    binding.constraintLayout.visibility=View.VISIBLE
                }
                R.id.myCreditFragment->{
                    binding.bottomNavigationView.visibility=View.VISIBLE
                    binding.constraintLayout.visibility=View.VISIBLE
                }
                R.id.myScheduleFragment->{
                    binding.tvTitle.text="Schedule"
                    binding.bottomNavigationView.visibility=View.VISIBLE
                    binding.constraintLayout.visibility=View.VISIBLE
                }
                R.id.myApponitmentFragment->{
                    binding.tvTitle.text="All Appointement"
                    binding.bottomNavigationView.visibility=View.VISIBLE
                    binding.constraintLayout.visibility=View.VISIBLE
                }
                else ->{
                    binding.bottomNavigationView.visibility=View.GONE
                    binding.constraintLayout.visibility=View.GONE
                }
            }
        }
    }

    fun closeNavDraw()
    {
        binding.drawer.close()
    }

    override fun onBackPressed() {
        if (binding.drawer.isOpen)
        {
            closeNavDraw()
        }else{
            super.onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }





}
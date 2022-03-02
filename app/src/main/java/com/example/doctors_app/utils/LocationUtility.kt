package com.example.doctors_app.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import pub.devrel.easypermissions.EasyPermissions

object LocationUtility {

    fun hasLocationPermission(context: Context)=
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
        {
            Log.d("moalilog","<Q")
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            Log.d("moalilog","Q")
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
}
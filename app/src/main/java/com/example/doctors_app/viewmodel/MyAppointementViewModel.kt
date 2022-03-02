package com.example.doctors_app.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import com.example.doctors_app.data.Repo
import com.example.doctors_app.models.Appointeiment
import com.example.doctors_app.models.Medecation
import com.example.doctors_app.models.MedicalTest
import com.example.doctors_app.utils.MyUtils
import com.example.doctors_app.utils.MyUtils.handledata
import com.example.doctors_app.utils.StatusResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyAppointementViewModel @Inject constructor(
    private val repo: Repo,
    application: Application
): AndroidViewModel(application) {

    private var _myAppointment= MutableStateFlow<StatusResult<MutableList<Appointeiment>>?>(null)
    val myAppointment:StateFlow<StatusResult<MutableList<Appointeiment>>?> =_myAppointment

    private var _todayAppointment= MutableStateFlow<StatusResult<MutableList<Appointeiment>>?>(null)
    val todayAppointment:StateFlow<StatusResult<MutableList<Appointeiment>>?> =_todayAppointment

    private var _medicalTestUser:MutableStateFlow<StatusResult<MutableList<MedicalTest>>?> =
        MutableStateFlow(null)
    private var _medicationUser:MutableStateFlow<StatusResult<MutableList<Medecation>>?> =
        MutableStateFlow(null)
    val medicalTestUser:StateFlow<StatusResult<MutableList<MedicalTest>>?> = _medicalTestUser
    val medicationUser:StateFlow<StatusResult<MutableList<Medecation>>?> = _medicationUser


    fun getMyAppointment(){
        _myAppointment.value=StatusResult.OnLoading()
        if (hasInternetConnection())
        {
            repo.firebaseSource.getMyAppointemnt().addSnapshotListener { value, error ->
                if (error==null&&value!=null)
                {
                    _myAppointment.value=MyUtils.handledata(value)
                }
                else{
                    _myAppointment.value=StatusResult.OnError("${error?.message}")
                }
            }
        }else{
            _myAppointment.value=StatusResult.OnError("No internet connection")
        }
    }

    fun getTodayAppointement(){
        _todayAppointment.value=StatusResult.OnLoading()
        if (hasInternetConnection())
        {
            repo.firebaseSource.getMyAppointemnt().addSnapshotListener { value, error ->
                if (error==null&&value!=null)
                {
                    _todayAppointment.value=MyUtils.handledataAppointementForDay(value)
                }
                else{
                    _todayAppointment.value=StatusResult.OnError("${error?.message}")
                }
            }
        }else{
            _todayAppointment.value=StatusResult.OnError("No internet connection")
        }
    }


    fun getUserMedication(userId:String){
        if (hasInternetConnection()) {
            _medicationUser.value=StatusResult.OnLoading()
            repo.firebaseSource.getUserMedication(userId).addSnapshotListener { value, error ->
                if (error == null) {
                    _medicationUser.value=handledata<Medecation>(value)
                } else {
                    _medicationUser.value=StatusResult.OnError("No internet Connection")
                }
            }
        } else {
            _medicationUser.value=StatusResult.OnError("No internet Connection")
        }
    }
    fun getUserMedicalTest(userId:String){
        _medicalTestUser.value=StatusResult.OnLoading()
        if (hasInternetConnection()) {
            repo.firebaseSource.getUserMedicalTest(userId).addSnapshotListener { value, error ->
                if (error == null) {
                    _medicalTestUser.value=handledata<MedicalTest>(value)
                } else {
                    _medicalTestUser.value=StatusResult.OnError("No internet Connection")
                }
            }
        } else {
            _medicalTestUser.value=StatusResult.OnError("No internet Connection")
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManger = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkActive = connectivityManger.activeNetwork ?: return false
        val networkCapability =
            connectivityManger.getNetworkCapabilities(netWorkActive) ?: return false
        when {
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            else -> return false
        }

    }
}
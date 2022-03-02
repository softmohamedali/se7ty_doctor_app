package com.example.doctors_app.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.doctors_app.data.Repo
import com.example.doctors_app.models.TimeSchedule
import com.example.doctors_app.utils.MyUtils
import com.example.doctors_app.utils.StatusResult
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MySchedulaViewModel  @Inject constructor(
    private val repo: Repo,
    application: Application
): AndroidViewModel(application) {
    private var _IsDataSchedulaAdded:MutableLiveData<StatusResult<TimeSchedule>> = MutableLiveData()
    private var _IsDataSchedulaDelted:MutableLiveData<StatusResult<TimeSchedule>> = MutableLiveData()
    private var _DataSchedul:MutableLiveData<StatusResult<MutableList<TimeSchedule>>> = MutableLiveData()

    val isDataSchedulaAdded:LiveData<StatusResult<TimeSchedule>> = _IsDataSchedulaAdded
    val isDataSchedulaDelted:LiveData<StatusResult<TimeSchedule>> = _IsDataSchedulaDelted
    var dataSchedul:LiveData<StatusResult<MutableList<TimeSchedule>>> = _DataSchedul

    fun addTimeSchedula(time:TimeSchedule){
        _IsDataSchedulaAdded.value=StatusResult.OnLoading()
        if(hasInternetConnection())
        {
            repo.firebaseSource.addTimeSchedula(time).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    _IsDataSchedulaAdded.value=StatusResult.OnSuccess()
                }else{
                    _IsDataSchedulaAdded.value=StatusResult.OnError(it.exception?.message.toString())
                }
            }
        }else{
            _IsDataSchedulaAdded.value=StatusResult.OnError("No Internet Connection")
        }
    }

    fun getAllDataSchedula(){
        _DataSchedul.value=StatusResult.OnLoading()
        if (hasInternetConnection())
        {
            repo.firebaseSource.getAllTimeSchedula()
                .addSnapshotListener { value, error ->
                    if (error==null)
                    {
                        _DataSchedul.value=MyUtils.handledataTimeShedula(value){
                            delteDateSchedula(it)
                            Log.d("mylg","delete")
                        }
                    }else{
                        _DataSchedul.value=StatusResult.OnError(error.message.toString())
                    }
                }
        }
        else{
            _DataSchedul.value=StatusResult.OnError("No Internet Connection")
        }
    }

    fun delteDateSchedula(date: TimeSchedule){
        _IsDataSchedulaDelted.value=StatusResult.OnLoading()
        if(hasInternetConnection())
        {
            repo.firebaseSource.dleteTimeSchedula(date).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    _IsDataSchedulaDelted.value=StatusResult.OnSuccess()
                }else{
                    _IsDataSchedulaDelted.value=StatusResult.OnError(it.exception?.message.toString())
                }
            }
        }else{
            _IsDataSchedulaDelted.value=StatusResult.OnError("No Internet Connection")
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